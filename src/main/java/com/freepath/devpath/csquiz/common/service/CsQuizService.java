package com.freepath.devpath.csquiz.common.service;

import com.freepath.devpath.csquiz.common.dto.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.common.dto.CsQuizOptionCreateRequest;
import com.freepath.devpath.csquiz.common.entity.CsQuiz;
import com.freepath.devpath.csquiz.common.entity.CsQuizOption;
import com.freepath.devpath.csquiz.common.repository.CsQuizOptionRepository;
import com.freepath.devpath.csquiz.common.repository.CsQuizRepository;
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
}
