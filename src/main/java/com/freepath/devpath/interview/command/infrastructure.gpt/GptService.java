package com.freepath.devpath.interview.command.infrastructure.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private final WebClient webClient;

    /* 카테고리 선택으로 면접방 생성하고 첫 질문 도출*/
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
        String response = webClient.post()
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
                .onErrorResume(error -> {
                    System.out.println("[GPT 질문 오류] " + error.getMessage());
                    return Mono.just("GPT 질문 생성 중 오류가 발생했습니다.");
                })
                .block();

        return response;
    }

}