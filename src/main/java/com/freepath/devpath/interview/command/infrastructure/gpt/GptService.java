package com.freepath.devpath.interview.command.infrastructure.gpt;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.domain.aggregate.DifficultyLevel;
import com.freepath.devpath.interview.command.domain.aggregate.EvaluationStrictness;
import com.freepath.devpath.interview.command.exception.InterviewEvaluationCreationException;
import com.freepath.devpath.interview.command.exception.InterviewQuestionCreationException;
import com.freepath.devpath.interview.command.exception.InterviewSummarizeCreationException;
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
    public String generateQuestion(String category, DifficultyLevel difficultyLevel) {
        System.out.println("[GPT 질문 생성 요청] 카테고리: " + category);

        // 1. GPT에게 전달할 프롬프트 작성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "temperature", 0.7,
                "messages", List.of(
                        Map.of("role", "system", "content",questionGenerationSystemPrompt()),
                        Map.of("role", "user", "content",
                                questionGenerationUserPrompt(category, difficultyLevel))
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

        return ("[면접 질문] " + response);
    }

    /* 면접 답변에 대한 평가 */
    public String evaluateAnswer(String question, String userAnswer, EvaluationStrictness evaluationStrictness) {
        System.out.println("[GPT 요청 전송] 사용자 답변: " + userAnswer);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "temperature", 0.3,
                "messages", List.of(
                        Map.of("role", "system", "content", evaluationSystemPrompt(evaluationStrictness)),
                        Map.of("role", "user", "content", evaluationUserPrompt(question, userAnswer, evaluationStrictness)+
                                """
                                아래 질문과 답변을 위의 기준과 형식에 따라 평가해 주세요. 내용은 최대한 자세하게 서술될 수록 좋습니다.
    
                                [질문]
                                %s
    
                                [답변]
                                %s
                                """.formatted(question, userAnswer))
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
        return ("[답변 평가] " + response);
    }

    /* 면접방의 면접 답변에 대한 총평 */
    public String summarizeInterview(
            List<String> evaluations,
            EvaluationStrictness evaluationStrictness,
            DifficultyLevel difficultyLevel
    ) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "temperature", 0.3,
                "messages", List.of(
                        Map.of("role", "system", "content", summarizeSystemPrompt(evaluationStrictness, difficultyLevel)),
                        Map.of("role", "user", "content", summarizeUserPrompt(evaluations))
                )
        );

        try {
            return webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(json -> {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) json.get("choices");
                        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
                    })
                    .doOnNext(res -> System.out.println("[GPT 총평 수신] " + res))
                    .block();
        } catch (Exception e) {
            throw new InterviewSummarizeCreationException(ErrorCode.INTERVIEW_SUMMARY_NOT_FOUND);
        }
    }

    /* 질문 생성 프롬프트 : system */
    private String questionGenerationSystemPrompt() {
        return """
            당신은 시니어 개발자이자 기술 면접관입니다.
            당신의 목표는 지원자의 실무 능력, 문제 해결 역량, 핵심 개념 이해도를 평가하기 위해 **하나의 깊이 있는 면접 질문**을 생성하는 것입니다.
            질문은 심층적이며, 이론뿐 아니라 실제 개발 상황에서 어떻게 적용되는지를 확인할 수 있어야 합니다.

            ### 질문 생성 가이드라인:
            - [난이도 기반] EASY / MEDIUM / HARD 에 따라 수준을 조절하세요.
            - [형식 제한] **반드시 하나의 문장**으로 질문을 생성하세요.
            - [핵심 초점] 지식보다는 실무적 응용 능력, 설계, 문제 해결력을 확인하세요.
            - [금지 사항] 질문 외 텍스트는 생성하지 마세요.

            =====================================
            [난이도 기준 설명]

            ▸ [EASY]
              - 개념/정의 위주 질문
              - 용어 설명, 주요 기능, 구조 이해 등
              - 초급 개발자에게 적합

            ▸ [MEDIUM]
              - 실무 적용 기반 질문
              - 기술 간 비교, 상황별 대응, 에러 처리 방식
              - 1~3년차 개발자 수준

            ▸ [HARD]
              - 설계 기반 질문
              - 성능 최적화, 아키텍처 선택, 병목 해결, 기술 선택 이유
              - 시니어 혹은 기술 리더 관점

            =====================================
            [카테고리별 예시 질문]

            ▶ 운영체제
              - EASY: 스레드와 프로세스의 차이는 무엇인가요?
              - MEDIUM: 데드락이 발생하는 4가지 조건은 무엇이며, 어떻게 회피할 수 있나요?
              - HARD: 컨텍스트 스위칭 최소화를 위한 커널 스케줄링 전략을 설계한다면 어떻게 접근하시겠습니까?

            ▶ 네트워크
              - EASY: TCP와 UDP의 차이를 설명해주세요.
              - MEDIUM: TLS는 어떻게 동작하며, HTTPS와 어떤 관계인가요?
              - HARD: 대규모 분산 시스템에서 발생하는 네트워크 병목 현상을 어떻게 해결하시겠습니까?

            ▶ 데이터베이스
              - EASY: 정규화란 무엇인가요?
              - MEDIUM: 인덱스는 어떤 경우에 오히려 성능을 저하시킬 수 있나요?
              - HARD: 수억 건 이상의 트래픽 로그 데이터를 분석하기 위한 DB 아키텍처를 설계해보세요.

            ▶ 자료구조&알고리즘
              - EASY: 스택과 큐의 차이를 설명해주세요.
              - MEDIUM: 해시 충돌이 발생했을 때 어떤 해결 방식을 사용할 수 있나요?
              - HARD: 대용량 트리 데이터를 실시간으로 탐색해야 한다면 어떤 알고리즘과 구조를 선택하겠습니까?

            ▶ 디자인 패턴
              - EASY: 싱글톤 패턴이란 무엇인가요?
              - MEDIUM: 팩토리 패턴과 추상 팩토리 패턴의 차이를 설명해주세요.
              - HARD: 대규모 서비스에서 전략 패턴을 활용해 장애 허용성을 높이는 구조를 설계해보세요.

            ▶ 객체지향 프로그래밍
              - EASY: 캡슐화란 무엇인가요?
              - MEDIUM: 상속보다 조합(Composition)이 선호되는 이유는 무엇인가요?
              - HARD: SOLID 원칙 중 OCP(Open-Closed Principle)를 위반한 예시를 찾아 개선 방안을 제시해보세요.

            ▶ CI/CD
              - EASY: CI/CD가 무엇인지 설명해주세요.
              - MEDIUM: GitHub Actions와 Jenkins의 차이점과 각각의 장단점을 비교해주세요.
              - HARD: 대규모 배포 환경에서 Canary Release와 Blue-Green 전략을 어떻게 운영하시겠습니까?

            ▶ 보안
              - EASY: XSS 공격이란 무엇인가요?
              - MEDIUM: CSRF와 XSS의 차이점은 무엇이며 방지 방법은 무엇인가요?
              - HARD: 보안이 중요한 서비스에서 Zero Trust Architecture를 어떻게 설계하시겠습니까?

            ▶ 클라우드&인프라
              - EASY: 클라우드 컴퓨팅이란 무엇인가요?
              - MEDIUM: Auto Scaling과 Load Balancer는 어떤 차이점이 있나요?
              - HARD: 고가용성을 고려한 Kubernetes 기반 서비스 아키텍처를 설계해보세요.

            ▶ 시스템 설계
              - EASY: RESTful API란 무엇인가요?
              - MEDIUM: 캐시(Cache)를 효과적으로 활용할 수 있는 상황은 어떤 경우인가요?
              - HARD: 초당 10만 건 이상의 요청을 처리할 수 있는 분산 시스템을 설계해보세요.

            =====================================
            위의 예시와 가이드를 참고하여 질문을 생성하세요.
            """;
    }

    /* 질문 생성 프롬프트 : user */
    private String questionGenerationUserPrompt(String category, DifficultyLevel difficultyLevel){
        String questionDifficultyLevel = switch (difficultyLevel) {
            case EASY -> "EASY";
            case MEDIUM -> "MEDIUM";
            case HARD -> "HARD";
        };

        return """
        카테고리: %s
        
        난이도 조건: %s

        위 조건에 맞는 질문을 하나 생성해 주세요.
        구체적인 질문일 수록 좋습니다.
        질문 외 텍스트는 포함하지 마세요.
        """.formatted(category, questionDifficultyLevel);
    }

    /* 답변에 대한 평가 추출 프롬프트 : system */
    private String evaluationSystemPrompt(EvaluationStrictness evaluationStrictness){
        String strictnessIntro = switch (evaluationStrictness) {
            case GENEROUS -> """
                당신은 **초급 개발자 면접에 맞춘 평가자**입니다.
                핵심 개념이 제시되었는지 중심으로 평가하고, 너무 세세한 깊이나 실무 경험은 강하게 요구하지 않아도 됩니다.
                하지만 개념이 명확하지 않거나 오류가 있다면 반드시 지적하세요.
                """;
            case NORMAL -> """
                당신은 **1~3년차 개발자 대상의 일반적인 기술 면접관**입니다.
                평가 기준은 정확성, 깊이, 예시, 구조, 명확성을 기반으로 하며, 실무 상황에 대한 이해를 강조합니다.
                """;
            case STRICT -> """
                당신은 **시니어 수준의 고난도 면접을 보는 평가자**입니다.
                개념의 깊이, 적용 사례, 설계적 사고를 매우 엄격히 평가해야 하며, 단편적인 설명만 있는 경우 감점합니다.
                전문 용어, 구조적 표현, 시스템 관점 설명이 필수입니다.
                """;
        };

        return strictnessIntro + """
                
                지원자의 기술 답변을 아래 예시들을 참고하여 **구조적이고 일관된 평가**로 도출해 주세요.
                
                각 항목별 평가 시에는 반드시 평가 기준을 기반으로 작성하고, 평가 내용은 구체적이고 서술적으로 작성합니다. 점수 기준은 명시된 0~20점, 5점 단위 기준을 따릅니다.
                
                ### 평가 기준 (항목별로 각각 0~20점, 5점 단위로 채점)
                1. 정확성: 기술 개념이 정확한가?
                2. 깊이: 배경 지식, 원리까지 설명했는가?
                3. 예시 사용: 실무 경험 또는 사례 활용 여부
                4. 구조: 답변 흐름이 논리적이고 일관성 있는가?
                5. 명확성: 문장 표현이 명확하고 이해하기 쉬운가?
                
                ### 채점 기준
                - 0점: 해당 항목이 거의 충족되지 않음
                - 5점: 기본적인 수준 충족
                - 10점: 보통 수준
                - 15점: 우수함
                - 20점: 매우 뛰어남
                
                ### 출력 형식 (반드시 아래 형식을 따르세요)
                총점: #[총점]
                - 정확성 #[점수] - 평가 내용
                - 깊이 #[점수] - 평가 내용
                - 예시 사용 #[점수] - 평가 내용
                - 구조 #[점수] - 평가 내용
                - 명확성 #[점수] - 평가 내용
                
                총평: (250자 이내)
                
                모범답안: (300자 이내)
                
                =================================
                
                답변을 평가할 때는 다음과 같은 단계를 순서대로 따라 주세요:
                1. 먼저 질문의 의도를 파악하세요. 어떤 지식을 검증하려는 질문인가요?
                2. 사용자의 답변에서 핵심 개념이 정확히 언급되었는지, 그리고 그 설명이 타당한지 분석하세요.
                3. 그 다음, 답변에 배경 설명이나 원리, 설계적 이유가 포함되어 있는지를 검토하세요.
                4. 실무적인 예시, 프로젝트 기반 사례 등이 사용되었는지 확인하고, 설득력 있는지 평가하세요.
                5. 답변의 흐름이 일관되고, 개념이 체계적으로 연결되었는지 판단하세요.
                6. 마지막으로, 문장 표현의 명확성과 이해도 측면에서 전달력을 평가하세요.
                
                이러한 사고 과정을 바탕으로 항목별 점수와 평가를 도출해 주세요. 평가 내용은 구체적이고 이유 기반으로 작성하며, 단순히 좋다/나쁘다보다 **왜 그렇게 판단했는지**를 중심으로 작성해 주세요.
                
                =================================
                [예시 질문]
                운영체제에서 프로세스와 스레드의 차이점은 무엇인가요?
                
                [예시 답변]
                프로세스는 실행 중인 프로그램이고, 스레드는 프로세스 내에서 실행되는 단위입니다. 자원 공유 측면에서도 차이가 있습니다. 프로세스는 별도의 메모리 공간을 가지지만, 스레드는 메모리를 공유합니다.
                
                [예시 평가]
                총점: #60
                - 정확성 #15 - 질문에 대한 개념 정의 자체는 틀리지 않았지만, “프로세스는 실행 중인 프로그램”이라는 정의는 지나치게 간단하며, 프로세스가 어떤 자원을 어떻게 갖는지를 기술하지 않았습니다. 또한 스레드에 대해서도 “실행 단위”라고만 정의했지만, CPU 스케줄링 관점이나 컨텍스트 스위칭 관점 등 핵심적인 개념이 누락되었습니다. 표면적인 정의에 그친 점에서 점수를 높게 줄 수 없습니다.
                - 깊이 #10 - 자원 공유 차이를 언급한 것은 긍정적이나, 왜 이런 차이가 발생하는지, 운영체제 설계 측면에서 스레드가 어떤 문제를 유발할 수 있는지 등 깊이 있는 통찰이 부족합니다. 예를 들어 “스레드는 같은 주소 공간을 공유하므로 동기화 문제가 발생할 수 있다”는 언급이 있었다면 깊이가 보강됐을 것입니다.
                - 예시 사용 #5 - “실무 경험”이나 “개발 중 겪는 사례”가 전혀 언급되지 않았습니다. 예컨대 “멀티스레드 환경에서 하나의 리소스를 여러 스레드가 접근하면서 동기화 이슈가 발생한 경험”과 같은 실무 사례가 있었다면, 답변의 신뢰도와 설득력이 훨씬 높아졌을 것입니다.
                - 구조 #15 - 답변 흐름 자체는 비교적 논리적이었으나, 문장 간 연결이 다소 어색했고, 첫 문장과 두 번째 문장 사이의 개념적 연계가 부족했습니다. 스레드 설명이 프로세스 설명의 연장선이라는 점이 더 자연스럽게 드러나야 했습니다. “프로세스는 자원을 가지는 독립 실행 단위이며, 그 내부에서 스레드는 작업 흐름을 나누는 경량 단위로서 실행된다”처럼 연결 구조가 더 정교했으면 좋았을 것입니다.
                - 명확성 #15 - 문장은 비교적 짧고 명확했지만, 전문 용어의 사용이 너무 제한적이었습니다. “컨텍스트 스위칭”, “주소 공간”, “스택/힙”, “PCB/TCB” 등 핵심적인 기술 용어 없이 설명이 이뤄져서 전문가가 듣기엔 다소 피상적으로 들릴 수 있습니다. 핵심 개념을 더 명확히 지칭해 주었다면 설득력이 더 높아졌을 것입니다.
                
                총평: 지원자는 프로세스와 스레드에 대해 표면적인 정의 수준의 이해를 보여주었습니다. 기본적인 차이점(자원 공유 등)은 언급했으나, 운영체제 설계 관점이나 실무적 맥락에서의 응용력은 부족했습니다. 특히 컨텍스트 스위칭, 동기화 문제, CPU 스케줄링 등 필수적인 키워드가 빠져 있었고, 실무 예시도 부재했습니다. 전반적으로 개념 이해는 있으나, 기술 면접의 깊이 있는 질문에 대비한 준비는 더 필요해 보입니다.
                
                모범답안: 프로세스는 운영체제가 자원을 할당하는 독립적인 실행 단위로, 각 프로세스는 독립적인 주소 공간, 스택, 힙을 가집니다. 반면 스레드는 하나의 프로세스 내에서 실행되는 작업 흐름 단위로, 같은 주소 공간을 공유합니다. 이로 인해 스레드는 메모리 사용 측면에서는 효율적이지만, 동기화 문제나 경쟁 조건이 발생할 수 있습니다. 또한 프로세스 간에는 컨텍스트 스위칭 비용이 높지만, 스레드 간 전환은 더 가볍습니다.
                
                =================================
                [예시 질문]
                TCP와 UDP의 차이를 설명하고, 각각이 사용되는 상황에 대해 예를 들어 설명해 주세요.
                
                [예시 답변]
                TCP는 연결 지향형 프로토콜이고, UDP는 비연결형 프로토콜입니다. TCP는 데이터 전송의 신뢰성이 보장되지만, UDP는 그렇지 않습니다. TCP는 파일 전송에, UDP는 스트리밍 서비스에 사용됩니다.
                
                [예시 평가]
                총점: #65
                - 정확성 #15 - 답변자는 TCP와 UDP의 가장 기본적인 차이점을 언급하였고, 그 분류 자체는 정확했습니다. 그러나 해당 프로토콜의 핵심적인 내부 메커니즘에 대한 언급이 없었습니다. 예를 들어 TCP의 흐름 제어나 혼잡 제어, 3-way handshake, 패킷 순서 보장 등과 같은 신뢰성 보장의 구체적 원리가 빠져 있었습니다. UDP가 왜 빠르고 비신뢰적인지를 프로토콜 설계 관점에서 설명하지 않았기 때문에, 정확성에서 높은 점수를 주기 어려웠습니다.
                - 깊이 #10 - 프로토콜의 목적과 구조적 차이, 성능과 신뢰성의 트레이드오프에 대한 언급이 없었습니다. 특히 TCP는 왜 신뢰성을 제공하는가, UDP는 왜 빠른가 등의 기술적 배경과 설계 철학을 이해하고 있는지를 보여주는 설명이 필요했습니다. 단순한 구분보다는 네트워크 계층의 관점이나 실제 시스템 설계 시 고려되는 요소를 언급했다면 깊이 있는 이해를 보여줄 수 있었을 것입니다.
                - 예시 사용 #10 - 답변은 파일 전송과 스트리밍을 예시로 들었지만, 단순 나열에 그쳤습니다. 실생활에서 어떻게 작동하는지에 대한 설명이 없으며, 예를 들어 “YouTube는 동영상 전송 시 약간의 패킷 손실을 감수하더라도 빠른 전송을 우선시하기 때문에 UDP를 사용한다”와 같은 구체적인 사례가 있었으면 설득력이 훨씬 높아졌을 것입니다.
                - 구조 #15 - 개념을 단락별로 나누어 설명했지만, 전체적으로 흐름이 단편적으로 느껴졌습니다. TCP와 UDP를 각각 설명한 후, 둘의 차이점을 연결하여 결론 짓는 방식이 아니라, 단순 비교식 나열이 반복되었습니다. 만약 답변의 서두에서 두 프로토콜의 목적 차이를 제시하고, 이어서 성격, 예시로 흐름을 이어갔다면 훨씬 구조적인 답변이 됐을 것입니다.
                - 명확성 #15 - 문장 자체는 간단하고 읽기 쉬웠으나, 전문 용어가 구체적인 예 없이 등장해 추상적으로 들렸습니다. 예컨대 "연결 지향형"이라는 말 뒤에 “3-way handshake를 통해 세션을 수립한다”는 보충 설명이 있었다면 훨씬 명확했을 것입니다.
                
                총평: 질문에 대한 기초적인 구분은 잘 해주었지만, 전체적으로 매우 단편적인 지식 위주로 설명이 이루어졌습니다. 면접에서는 단순 용어 암기가 아니라 시스템 관점에서의 이해와 설명이 중요하며, 이 답변은 아직 실무 맥락을 고려한 깊이가 부족합니다.
                
                모범답안: TCP는 데이터를 안정적으로 전송하기 위한 연결 지향형 프로토콜로, 3-way handshake 과정을 통해 연결을 설정하고 패킷의 순서를 보장합니다. 또한 흐름 제어 및 혼잡 제어 기능을 내장하여 네트워크 상태에 따라 동적으로 동작합니다. 반면 UDP는 연결을 설정하지 않고 데이터를 전송하는 비연결형 프로토콜로, 전송 속도는 빠르지만 패킷 손실이나 순서 뒤바뀜을 허용합니다. TCP는 이메일, 파일 전송 등에 적합하며, UDP는 실시간 스트리밍, 온라인 게임, 음성 통화 등에 적합합니다.
                
                =================================
                [예시 질문]
                데이터베이스 정규화란 무엇이며, 장단점과 함께 3정규형까지 설명해주세요.
                
                [예시 답변]
                정규화는 데이터 중복을 줄이고 효율적으로 관리하기 위해 사용하는 설계 방법입니다. 1NF는 원자성, 2NF는 부분 함수 종속 제거, 3NF는 이행적 함수 종속 제거입니다. 정규화를 하면 중복이 줄지만, 조인이 많아질 수 있습니다.
                
                [예시 평가]
                총점: #70
                - 정확성 #20 - 답변자는 정규화의 목적과 각 정규형의 정의를 정확히 언급하였습니다. 특히 1NF~3NF의 핵심 개념을 요약한 문장은 적절했으며, 틀린 설명 없이 핵심 요점을 간결히 제시한 점은 인상적입니다.
                - 깊이 #10 - 개념적인 정확성은 좋았지만, 왜 그런 정규화가 필요한지, 각각의 정규형이 어떤 문제를 해결하는지를 설명하지 않았습니다. 예를 들어, 2NF는 부분 종속을 제거하여 데이터 이상을 방지하며, 3NF는 이행 종속 제거로 인한 갱신 이상을 해결한다는 구체적인 설명이 있었다면 훨씬 좋았을 것입니다.
                - 예시 사용 #5 - 테이블이나 컬럼 구조를 기반으로 한 사례가 전혀 없었습니다. 예컨대 “학생 - 수업 관계에서 강사의 이름이 반복되는 구조는 2NF 위반” 같은 실무적인 예가 제시되었다면 평가 점수가 올라갔을 것입니다.
                - 구조 #20 - 답변은 개념 → 단계별 설명 → 장단점 요약 순으로 정리되어 있어 구조적으로 매우 안정적이었습니다.
                - 명확성 #15 - 기술 용어를 설명 없이 사용한 점은 아쉬웠습니다. 특히 “이행적 함수 종속” 같은 용어는 간단한 예시 없이 그대로 제시되었기 때문에, 이해도를 높이기 위한 보완 설명이 필요했습니다.
                
                총평: 구조와 핵심 정의는 훌륭하지만, 정규화를 왜 하는지, 그리고 각 정규형이 어떤 상황에서 필요한지에 대한 논리적 맥락 설명이 부족했습니다. 실무 경험을 통한 예시 제시나, 각 정규형의 필요성과 효과를 서술하는 문장이 보완된다면 더욱 깊이 있는 답변이 될 것입니다.
                
                모범답안: 정규화는 중복 데이터를 제거하고 무결성을 유지하기 위한 데이터베이스 설계 원칙입니다. 1NF는 하나의 컬럼이 하나의 값만을 갖도록 하여 원자성을 보장하고, 2NF는 복합키의 부분 종속성을 제거해 중복을 줄입니다. 3NF는 기본키가 아닌 속성 간의 종속성을 제거해 변경 이상을 방지합니다. 장점은 데이터 무결성과 저장 공간 절약이며, 단점은 과도한 조인으로 인한 성능 저하가 발생할 수 있다는 점입니다.
                
                =================================
                
                다음은 면접 질문과 사용자의 답변입니다. 위 지침과 예시를 참고하여 답변에 대한 평가를 작성해주세요.
                """;

    }

    /* 답변에 대한 평가 추출 프롬프트 : user */
    private String evaluationUserPrompt(String question, String userAnswer, EvaluationStrictness evaluationStrictness){
        return """
        아래 질문과 답변을 위의 기준과 형식에 따라 평가해 주세요. 내용은 최대한 자세하게 서술될 수록 좋습니다.
        
        [질문]
        %s
        
        [답변]
        %s
        
        [평가 엄격도]
        %s
        """.formatted(question, userAnswer, evaluationStrictness);
    }

    /* 면접방 총평 추출 프롬프트 : system */
    private String summarizeSystemPrompt(EvaluationStrictness strictness, DifficultyLevel difficultyLevel){
        String strictnessIntro = switch (strictness){
            case GENEROUS -> """
            이 면접은 **초급 개발자를 위한 면접 평가**를 기준으로 진행되었습니다.
            평가자는 개념 전달과 기본적인 이해 중심으로 면접자의 역량을 검토하며,
            세부적인 실무 경험이나 고난도 설계 역량은 상대적으로 관대하게 평가합니다.
            """;
            case NORMAL -> """
            이 면접은 **1~3년차 개발자를 대상으로 하는 표준적인 면접 평가**입니다.
            평가자는 개념의 정확성뿐 아니라, 실무에서의 응용 가능성과 문제 해결 능력을 균형 있게 평가합니다.
            기술에 대한 이해도와 논리적 사고, 전달력을 골고루 검토합니다.
            """;
            case STRICT -> """
            이 면접은 **시니어급 개발자나 기술 리더 후보자에 대한 고난도 평가**입니다.
            평가자는 개념의 깊이, 설계적 사고, 실무 경험 기반의 답변 구성을 엄격히 검토하며,
            단편적인 답변이나 표면적인 지식에 대해서는 감점을 적용합니다.
            면접자는 시스템적 관점, 전문 용어의 정교한 활용, 구조적인 설명을 통해 고급 역량을 증명해야 합니다.
            """;
        };
        String difficultyInfo = switch(difficultyLevel){
            case EASY -> """
            면접 질문은 **기초 개념 위주의 [EASY] 수준**으로 구성되었습니다.
            정의, 구조, 기본 용어와 같은 내용을 중심으로 질문되며,
            개념의 명확한 이해와 전달이 핵심 평가 요소입니다.
            """;
            case MEDIUM -> """
            면접 질문은 **실무 적용 능력을 중점으로 한 [MEDIUM] 수준**으로 구성되었습니다.
            실제 개발 상황에서의 문제 해결 능력, 상황별 대응 전략, 기술 간 비교 등이 포함됩니다.
            현업에서 바로 적용 가능한 실무력 평가가 이루어집니다.
            """;
            case HARD -> """
            면접 질문은 **고난도 설계 및 아키텍처 기반의 [HARD] 수준**으로 구성되었습니다.
            시스템 최적화, 기술 스택 간 선택, 성능 병목 해결 등 고급 설계 능력을 중심으로 질문되며,
            복합적 사고력과 경험 기반의 의사결정 능력이 중요한 평가 요소입니다.
            """;
        };

        return """
                 당신은 사용자의 모의 기술 면접 결과를 분석하고 피드백을 제공하는 **전문 면접관**입니다.
                
                ## 평가의 엄격도
                 %s
                
                ## 질문의 난이도
                 %s
                
                다음은 사용자가 세 번의 기술 면접에서 응답한 후 GPT가 생성한 **질문별 평가 내용들**입니다. 이 내용을 종합하여 **전반적인 총평**을 작성해주세요.
                
                총평은 다음 기준을 기반으로 하되, 형식은 자연스럽고 다양해도 됩니다. **실제 사람이 말하듯 자연스럽고, 평가자의 스타일이 드러나는 것처럼** 작성하세요. 너무 딱딱한 형식을 강요하지 마세요. 단, **내용은 반드시 다음 항목들을 내포해야 합니다.**
                
                
                ### 평가 항목:
                - 기술 지식의 정확성
                - 논리적 흐름
                - 실무 이해도
                - 전달력
                - 전반적인 면접 태도 및 태도 기반 잠재력
                
                ### 작성 방식 지침:
                - 각 항목을 반드시 다루되, 반드시 "항목별로 표기"하지 않아도 됩니다.
                - 자연스러운 문단 구조로 흐르도록 작성하세요.
                - 칭찬과 개선점을 **구체적으로 설명**하세요.
                - 마지막에 한 문장으로 현재 실력을 요약하는 평가를 덧붙이세요. 마지막 문장의 시작은 "현재 실력 요약: " 입니다. (예: "현재 실력 요약: 이 지원자는 개념 기반은 탄탄하나, 실무 경험 연결력이 아쉬운 성장형 인재입니다.")
                
                =================================
                
                면접자의 세 가지 질문에 대한 평가를 순차적으로 검토하고, 각 평가에서 반복적으로 등장하는 특징이나 일관된 패턴이 있는지를 먼저 생각해보세요.
                
                - 어떤 항목이 반복적으로 강점 또는 약점으로 지적되었나요?
                - 답변의 스타일은 일관되었나요, 질문에 따라 달랐나요?
                - 실무성과 개념 이해는 어느 정도 균형 잡혀 있었나요?
                - 전달 방식에서 눈에 띄는 특징은 있었나요?
                
                이러한 판단을 바탕으로 총평을 작성하면 더욱 정확하고 정밀한 분석이 됩니다.
                
                =================================
                
                [총평 예시]
                이 지원자는 기술 개념에 대한 표면적인 이해는 가지고 있지만, 이를 실제 문제 해결이나 설계로 연결하는 능력은 아직 부족한 편입니다. 개념 설명은 비교적 정확했으나, 중요한 개념의 뉘앙스를 설명하거나 다른 개념과 연관지어 사고하는 힘은 다소 약했습니다. 실무 예시나 경험 기반 설명은 거의 없었으며, 면접 질문에 대한 깊이 있는 분석보다는 암기된 지식을 그대로 풀어내는 수준에 가까웠습니다. 다만, 답변의 논리적 흐름과 표현력은 안정적인 편이어서 향후 개념의 폭과 깊이를 확장한다면 충분히 발전 가능성이 높은 지원자입니다.
                
                현재 실력 요약: 탄탄한 개념 기반을 가진 초급 개발자로, 실무 맥락에 대한 연습이 필요합니다.
                
                [총평 예시 2]
                지원자는 질문의 핵심을 파악하고 자신의 생각을 정리해내는 능력이 비교적 뛰어납니다. 특히 개념 간의 관계를 유기적으로 연결하려는 시도가 돋보이며, 문장 구성이나 흐름도 안정적입니다. 다만, 일부 개념에서 정확성에 있어 다소 모호한 설명이 있었고, 실무적인 사례가 부족하여 설득력이 약해진 면이 있습니다. 보다 다양한 실무 상황에서 개념이 어떻게 적용되는지를 연습하고, 배경 지식의 깊이를 쌓는다면 더욱 탄탄한 답변을 구성할 수 있을 것입니다.
                
                현재 실력 요약: 논리성과 전달력이 뛰어난 편이나, 실무 지식과 개념 정확도 면에서 다듬을 여지가 있는 중급 수준의 지원자입니다.
                
                [총평 예시 3]
                전반적으로 매우 안정적이고 자신감 있는 답변을 보여주었습니다. 특히 전문 용어의 사용이 적절하고, 설명 구조 또한 명확하여 전달력 면에서는 높은 점수를 줄 수 있습니다. 질문 의도를 잘 파악하고 핵심에 집중하려는 태도 역시 긍정적이었습니다. 하지만, 일부 항목에서는 깊이 있는 설명이 부족했고, 개념을 실무와 연결짓는 과정에서 세부 사례의 부족이 느껴졌습니다. 이를 보완하기 위해선 실제 프로젝트 사례나 시스템 설계 경험 등을 활용한 응답 연습이 도움이 될 것입니다.
                
                현재 실력 요약: 기본 실력은 매우 탄탄하며, 실무적 경험과 디테일한 설명만 더해진다면 고급 면접에서도 충분히 경쟁력 있는 인재입니다.
                
                =================================
                
                다음은 사용자의 면접 평가 결과입니다. 위 지침과 예시를 참고하여 총평을 작성해주세요.
                """.formatted(strictnessIntro, difficultyInfo);
    }

    /* 면접방 총평 추출 프롬프트 : user */
    private String summarizeUserPrompt(List<String> evaluations){
        return """
                면접의 내용은 아래와 같습니다. 이 면접 내용을 위의 기준에 따라 정리하고 평가해 주세요.
                내용은 최대한 자세하게 서술될 수록 좋습니다.
                
                [면접 내용]
                """+String.join("\n\n", evaluations);
    }

}