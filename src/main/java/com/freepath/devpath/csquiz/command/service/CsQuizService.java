package com.freepath.devpath.csquiz.command.service;

import com.freepath.devpath.csquiz.command.dto.request.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizOptionCreateRequest;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizOptionUpdateRequest;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizUpdateRequest;
import com.freepath.devpath.csquiz.command.entity.CsQuiz;
import com.freepath.devpath.csquiz.command.entity.CsQuizOption;
import com.freepath.devpath.csquiz.command.repository.CsQuizOptionRepository;
import com.freepath.devpath.csquiz.command.repository.CsQuizRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));

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
