package com.freepath.devpath.interview.command.application.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.application.dto.request.InterviewAnswerCommandRequest;
import com.freepath.devpath.interview.command.application.dto.request.InterviewRoomUpdateCommandRequest;
import com.freepath.devpath.interview.command.application.dto.response.InterviewAnswerCommandResponse;
import com.freepath.devpath.interview.command.application.dto.response.InterviewRoomCommandResponse;
import com.freepath.devpath.interview.command.domain.aggregate.*;
import com.freepath.devpath.interview.command.domain.repository.InterviewRepository;
import com.freepath.devpath.interview.command.domain.repository.InterviewRoomRepository;
import com.freepath.devpath.interview.command.exception.*;
import com.freepath.devpath.interview.command.infrastructure.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

    private final InterviewRoomRepository interviewRoomRepository;
    private final InterviewRepository interviewRepository;
    private final GptService gptService;

    /* 카테고리 선택으로 면접방 생성하고 첫 질문 도출*/
    @Transactional
    public InterviewRoomCommandResponse createRoomAndFirstQuestion(
            Long userId,
            String category,
            DifficultyLevel difficultyLevel,
            EvaluationStrictness evaluationStrictness
    ) {

        // 0. 면접방 기본 정보 생성
        difficultyLevel = Optional.ofNullable(difficultyLevel)
                .orElse(DifficultyLevel.MEDIUM);
        evaluationStrictness = Optional.ofNullable(evaluationStrictness)
                .orElse(EvaluationStrictness.NORMAL);
        String title = category + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

        // 1. 면접방 생성 및 저장
        InterviewRoom room = null;
         try{
            room = interviewRoomRepository.save(
                    InterviewRoom.builder()
                            .userId(userId)
                            .interviewCategory(category)
                            .interviewRoomTitle(title)
                            .difficultyLevel(difficultyLevel)
                            .evaluationStrictness(evaluationStrictness)
                            .build()
            );
         } catch(Exception e){
             throw new InterviewRoomCreationException(ErrorCode.INTERVIEW_ROOM_CREATION_FAILED);
         }


        // 2. GPT로부터 첫 질문 생성
        String firstQuestion = gptService.generateQuestion(category, difficultyLevel);

        // 3. 질문 내용을 INTERVIEW 테이블에 저장
        interviewRepository.save(
                Interview.builder()
                        .interviewRoomId(room.getInterviewRoomId())
                        .interviewRole(Interview.InterviewRole.AI)
                        .interviewMessage(firstQuestion)
                        .build()
        );

        // 4. 응답
        return InterviewRoomCommandResponse.builder()
                .interviewRoomId(room.getInterviewRoomId())
                .firstQuestion(firstQuestion)
                .build();
    }

    /* 질문에 대한 답변, 답변에 대한 평가, 다음 질문 도출 */
    @Transactional
    public InterviewAnswerCommandResponse answerAndEvaluate(Long userId, Long roomId, InterviewAnswerCommandRequest request) {

        // 0-1. 면접방 존재 여부 확인
        InterviewRoom room = interviewRoomRepository.findById(roomId)
                .orElseThrow(() -> new InterviewRoomNotFoundException(ErrorCode.INTERVIEW_ROOM_NOT_FOUND));

        // 0-2. 면접방 진행자 검증
        if(!room.getUserId().equals(userId)){
            throw new InterviewRoomAccessException(ErrorCode.INTERVIEW_ROOM_ACCESS_DENIED);
        }

        // 0-3. 면접방 당 면접 개수 검증
        int interviewIndex = request.getInterviewIndex();
        if(interviewIndex<1 || interviewIndex>3){
            throw new InterviewIndexInvalidException(ErrorCode.INTERVIEW_INDEX_INVALID);
        }

        // 0-4. 면접 답변 내용 검증
        if(request.getUserAnswer() == null || request.getUserAnswer().isEmpty()){
            throw new InterviewAnswerEmptyException(ErrorCode.INTERVIEW_ANSWER_EMPTY);
        }

        // 1. 사용자의 답변 저장
        interviewRepository.save(
                Interview.builder()
                        .interviewRoomId(roomId)
                        .interviewRole(Interview.InterviewRole.USER)
                        .interviewMessage(request.getUserAnswer())
                        .build()
        );

        // 2. GPT 평가 생성
        Interview question
                = interviewRepository.findTopByInterviewRoomIdAndInterviewRoleOrderByInterviewIdDesc(
                            roomId, Interview.InterviewRole.AI)
                    .orElseThrow(
                            () -> new InterviewEvaluationCreationException(ErrorCode.INTERVIEW_EVALUATION_FAILED)
                    );
        String evaluation = gptService.evaluateAnswer(question.getInterviewMessage(), request.getUserAnswer());

        // 3. GPT 평가 저장
        interviewRepository.save(
                Interview.builder()
                        .interviewRoomId(roomId)
                        .interviewRole(Interview.InterviewRole.AI)
                        .interviewMessage(evaluation)
                        .build()
        );

        // 4. 다음 질문 생성 (면접방 당 3회 면접 실행)
        String nextQuestion = null;
        if (interviewIndex < 3) {
            nextQuestion = gptService.generateQuestion(room.getInterviewCategory(), room.getDifficultyLevel());
            interviewRepository.save(
                    Interview.builder()
                            .interviewRoomId(roomId)
                            .interviewRole(Interview.InterviewRole.AI)
                            .interviewMessage(nextQuestion)
                            .build()
            );
        }

        // 5. 마지막 답변이라면 면접 총평 생성하고 면접방 상태 변경
        if(interviewIndex == 3){
            List<String> gptEvaluations = interviewRepository.findByInterviewRoomId(roomId).stream()
                    .filter(interview -> interview.getInterviewRole() == Interview.InterviewRole.AI)
                    .map(Interview::getInterviewMessage)
                    .filter(msg -> msg.startsWith("[답변 평가]"))
                    .toList();
            String summary = gptService.summarizeInterview(gptEvaluations);

            interviewRepository.save(
                    Interview.builder()
                            .interviewRoomId(roomId)
                            .interviewRole(Interview.InterviewRole.AI)
                            .interviewMessage("[총평]"+summary)
                            .build()
            );

            room.updateStatus(InterviewRoomStatus.COMPLETED);
        }

        // 6. 응답
        return InterviewAnswerCommandResponse.builder()
                .interviewRoomId(roomId)
                .userAnswer(request.getUserAnswer())
                .gptEvaluation(evaluation)
                .nextQuestion(nextQuestion)
                .build();
    }

    /* 면접방 삭제 */
    @Transactional
    public void deleteInterviewRoom(Long userId, Long roomId) {

        // 면접방 존재 여부 확인
        InterviewRoom room = interviewRoomRepository.findById(roomId)
                .orElseThrow(() -> new InterviewRoomNotFoundException(ErrorCode.INTERVIEW_ROOM_NOT_FOUND));

        // 소유자 검증
        if (!room.getUserId().equals(userId)) {
            throw new InterviewRoomAccessException(ErrorCode.INTERVIEW_ROOM_ACCESS_DENIED);
        }

        try {
            // 1. 면접 이력 삭제
            interviewRepository.deleteByInterviewRoomId(roomId);

            // 2. 면접방 삭제
            interviewRoomRepository.deleteById(roomId);
        } catch (Exception e) {
            throw new InterviewRoomDeleteException(ErrorCode.INTERVIEW_ROOM_DELETE_FAILED);
        }
    }

    /* 면접방 정보 수정 */
    @Transactional
    public void updateInterviewRoom(Long userId, Long roomId, InterviewRoomUpdateCommandRequest request) {

        // 면접방 존재 여부 확인
        InterviewRoom room = interviewRoomRepository.findById(roomId)
                .orElseThrow(() -> new InterviewRoomNotFoundException(ErrorCode.INTERVIEW_ROOM_NOT_FOUND));

        // 면접방 진행자 검증
        if (!room.getUserId().equals(userId)) {
            throw new InterviewRoomAccessException(ErrorCode.INTERVIEW_ROOM_ACCESS_DENIED);
        }

        // 면접방 제목 수정
        if (request.getInterviewRoomTitle() == null || request.getInterviewRoomTitle().isEmpty() || request.getInterviewRoomTitle().isBlank() || request.getInterviewRoomTitle().trim().isEmpty()) {
            throw new InterviewRoomTitleInvalidException(ErrorCode.INTERVIEW_ROOM_TITLE_INVALID);
        }
        room.updateTitle(request.getInterviewRoomTitle());

        // 면접방 메모 수정
        room.updateMemo(request.getInterviewRoomMemo());
    }


}
