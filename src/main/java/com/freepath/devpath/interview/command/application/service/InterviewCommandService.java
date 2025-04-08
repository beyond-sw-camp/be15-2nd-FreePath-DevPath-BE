package com.freepath.devpath.interview.command.application.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.application.dto.request.InterviewAnswerCommandRequest;
import com.freepath.devpath.interview.command.application.dto.response.InterviewAnswerCommandResponse;
import com.freepath.devpath.interview.command.application.dto.response.InterviewRoomCommandResponse;
import com.freepath.devpath.interview.command.domain.aggregate.Interview;
import com.freepath.devpath.interview.command.domain.aggregate.InterviewRoom;
import com.freepath.devpath.interview.command.domain.repository.InterviewRepository;
import com.freepath.devpath.interview.command.domain.repository.InterviewRoomRepository;
import com.freepath.devpath.interview.command.exception.*;
import com.freepath.devpath.interview.command.infrastructure.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

    private final InterviewRoomRepository interviewRoomRepository;
    private final InterviewRepository interviewRepository;
    private final GptService gptService;

    /* 카테고리 선택으로 면접방 생성하고 첫 질문 도출*/
    @Transactional
    public InterviewRoomCommandResponse createRoomAndFirstQuestion(Long userId, String category) {

        // 1. 면접방 생성 및 저장
        InterviewRoom room = null;
         try{
            room = interviewRoomRepository.save(
                    InterviewRoom.builder()
                            .userId(userId)
                            .interviewCategory(category)
                            .build()
            );
         } catch(Exception e){
             throw new InterviewRoomCreationException(ErrorCode.INTERVIEW_ROOM_CREATION_FAILED);
         }


        // 2. GPT로부터 첫 질문 생성
        String firstQuestion = gptService.generateFirstQuestion(category);
         if(firstQuestion == null){
             throw new InterviewQuestionCreationException(ErrorCode.INTERVIEW_QUESTION_CREATION_FAILED);
         }

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
        if(request.getUserAnswer() == null){
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
        String evaluation = gptService.evaluateAnswer(request.getUserAnswer());
        if(evaluation == null){
            throw new InterviewEvaluationCreationException(ErrorCode.INTERVIEW_EVALUATION_FAILED);
        }

        // 3. GPT 평가 저장
        interviewRepository.save(
                Interview.builder()
                        .interviewRoomId(roomId)
                        .interviewRole(Interview.InterviewRole.AI)
                        .interviewMessage(evaluation)
                        .build()
        );

        // 4. 면접방 당 총 3회차의 면접 실행
        String nextQuestion = null;
        if (interviewIndex < 3) {
            nextQuestion = gptService.generateFirstQuestion(room.getInterviewCategory());
            interviewRepository.save(
                    Interview.builder()
                            .interviewRoomId(roomId)
                            .interviewRole(Interview.InterviewRole.AI)
                            .interviewMessage(nextQuestion)
                            .build()
            );
        }

        // 5. 응답
        return InterviewAnswerCommandResponse.builder()
                .interviewRoomId(roomId)
                .userAnswer(request.getUserAnswer())
                .gptEvaluation(evaluation)
                .nextQuestion(nextQuestion)
                .build();
    }
}
