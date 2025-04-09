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
    public String generateQuestion(String category) {
        System.out.println("[GPT 질문 생성 요청] 카테고리: " + category);

        // 1. GPT에게 전달할 프롬프트 작성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "당신은 시니어 개발자 또는 TPM 급의 고급 IT 인력이며, 기술 면접관입니다. "
                                        + "지원자의 실무 능력과 문제 해결 역량을 파악할 수 있도록 질문을 구성하세요. "
                                        + "질문은 심층적이며, 이론뿐 아니라 실제 개발 상황에서 어떻게 적용되는지를 확인할 수 있어야 합니다. "
                                        + "질문은 오직 하나만 생성하세요. 질문 외의 텍스트는 포함하지 마세요."),
                        Map.of("role", "user", "content",
                                category + "와 관련된 핵심 기술 또는 개념을 바탕으로, "
                                        + "경력 1~3년차 개발자에게 던질 수준의 실무형 면접 질문을 하나만 생성해주세요.")
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

        if(response == null || response.isEmpty()){
            throw new InterviewQuestionCreationException(ErrorCode.INTERVIEW_QUESTION_CREATION_FAILED);
        }

        return response;
    }

    /* 면접 답변에 대한 평가 */
    public String evaluateAnswer(String question, String userAnswer) {
        System.out.println("[GPT 요청 전송] 사용자 답변: " + userAnswer);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", """
                                    당신은 전문적인 기술 면접관입니다. 사용자의 면접 답변을 다음 기준으로 평가해 주세요:
                                    
                                    [질문]
                                    %s
                                    
                                    [답변]
                                    %s
                                    
                                    1. 정확성: 기술 개념이 정확하게 설명되었는가?
                                    2. 깊이: 배경 지식이나 원리까지 설명했는가?
                                    3. 예시 사용: 실제 경험이나 사례를 활용했는가?
                                    4. 구조: 답변의 흐름이 자연스럽고 논리적인가?
                                    5. 명확성: 문장이 명확하고 이해하기 쉬운가?
                                    
                                    평가 결과는 아래 형식에 맞게 작성해주세요:
                                    
                                    - 총점 (100점 만점): __점
                                    - 총평: (실제 면접관이 면접자를 평가하는 것처럼 3~4줄. 사용자가 보완하는 점 1~2줄 필수로 포함할 것.)
                                    - 항목별 평가:
                                      - 정확성:
                                      - 깊이:
                                      - 예시 사용:
                                      - 구조:
                                      - 명확성:
                                    - 모범 답안: (질문에 대한 가장 이상적인 답안을 5줄 내외로 작성)
                                    """.formatted(question, userAnswer)),
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
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
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

        if(response == null || response.isEmpty()){
            throw new InterviewEvaluationCreationException(ErrorCode.INTERVIEW_EVALUATION_FAILED);
        }
        return response;
    }

}