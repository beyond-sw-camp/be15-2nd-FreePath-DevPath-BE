package com.freepath.devpath.csquiz.command.application.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizOptionCreateRequest;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizOptionUpdateRequest;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizUpdateRequest;
import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuiz;
import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuizOption;
import com.freepath.devpath.csquiz.command.domain.repository.CsQuizOptionRepository;
import com.freepath.devpath.csquiz.command.domain.repository.CsQuizRepository;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CsQuizService {

    private final CsQuizRepository csQuizRepository;
    private final CsQuizOptionRepository csQuizOptionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void registerQuiz(CsQuizCreateRequest request) {
        // 퀴즈 저장
        CsQuiz csQuiz = modelMapper.map(request, CsQuiz.class);
        csQuizRepository.save(csQuiz);

        // 옵션 저장
        for (CsQuizOptionCreateRequest optionReq : request.getOptions()) {
            CsQuizOption option = new CsQuizOption();
            option.setCsquizId(csQuiz.getCsquizId());
            option.setOptionNo(optionReq.getOptionNo());
            option.setOptionContents(optionReq.getOptionContents());
            csQuizOptionRepository.save(option);
        }
    }

    @Transactional
    public void updateQuiz(int quizId, CsQuizUpdateRequest csQuizUpdateRequest) {
        CsQuiz csQuiz = csQuizRepository.findById(quizId)
                .orElseThrow(() -> new CsQuizNotFoundException(ErrorCode.CS_QUIZ_NOT_FOUND));

        csQuiz.setCsquizContents(csQuizUpdateRequest.getCsquizContents());
        csQuiz.setCsquizAnswer(csQuizUpdateRequest.getCsquizAnswer());
        csQuiz.setCsquizExplanation(csQuizUpdateRequest.getCsquizExplanation());
        csQuiz.setIsCsquizSubmitted(csQuizUpdateRequest.getIsCsquizSubmitted());
        csQuizRepository.save(csQuiz);

        csQuizOptionRepository.deleteByCsquizId(quizId);

        for (CsQuizOptionUpdateRequest optionRequest : csQuizUpdateRequest.getOptions()) {
            CsQuizOption option = new CsQuizOption();
            option.setCsquizId(quizId);
            option.setOptionNo(optionRequest.getOptionNo());
            option.setOptionContents(optionRequest.getOptionContents());
            csQuizOptionRepository.save(option);
        }
    }

}
