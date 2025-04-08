package com.freepath.devpath.interview.command.infrastructure.gpt;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.exception.InterviewEvaluationCreationException;
import com.freepath.devpath.interview.command.exception.InterviewQuestionCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private final WebClient webClient;

    /* 카테고리에 대한 첫 질문 도출*/
    public String generateFirstQuestion(String category) {
        System.out.println("[GPT 질문 생성 요청] 카테고리: " + category);

        // 1. GPT에게 전달할 프롬프트 작성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 기술 면접 질문 생성기로, 개발자로 취업하기를 희망하는 사용자와 모의 기술 면접을 진행 중입니다. 지정된 기술 카테고리에 대한 심층 질문을 하나 생성해 주세요."),
                        Map.of("role", "user", "content", category + " 관련 면접 질문을 하나 생성해 주세요.")
                )
        );

        // 2. GPT가 도출한 질문 저장
        String response = null;
        try{
            response = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(json -> {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) json.get("choices");
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    })
                    .doOnNext(res -> System.out.println("[GPT 질문 응답 수신] " + res))
                    .block();

        } catch(Exception e){
            throw new InterviewQuestionCreationException(ErrorCode.INTERVIEW_QUESTION_CREATION_FAILED);
        }

        if(response == null){
            throw new InterviewQuestionCreationException(ErrorCode.INTERVIEW_QUESTION_CREATION_FAILED);
        }

        return response;
    }

    /* 면접 답변에 대한 평가 */
    public String evaluateAnswer(String userAnswer) {
        System.out.println("[GPT 요청 전송] 사용자 답변: " + userAnswer);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "당신은 면접 평가자로,개발자로 취업하기를 희망하는 사용자와 모의 기술 면접을 진행 중입니다. 응답을 평가해 주세요."),
                        Map.of("role", "user", "content", userAnswer)
                )
        );

        String response = null;
        try {
            response = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .map(json -> {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) json.get("choices");
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content");
                    })
                    .doOnNext(res -> System.out.println("[GPT 응답 수신] " + res))
                    .block();

        } catch (Exception e){
            throw new InterviewEvaluationCreationException(ErrorCode.INTERVIEW_EVALUATION_FAILED);
        }

        if(response == null){
            throw new InterviewEvaluationCreationException(ErrorCode.INTERVIEW_EVALUATION_FAILED);
        }
        return response;
    }

}