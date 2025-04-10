package com.freepath.devpath.interview.command.infrastructure.gpt;

import com.freepath.devpath.common.exception.ErrorCode;
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

        return ("[면접 질문] " + response);
    }

    /* 면접 답변에 대한 평가 */
    public String evaluateAnswer(String question, String userAnswer) {
        System.out.println("[GPT 요청 전송] 사용자 답변: " + userAnswer);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", """
                                당신은 시니어 개발자로서 기술 면접관 역할을 맡고 있습니다. 지원자의 기술 답변을 아래 예시들을 참고하여 **구조적이고 일관된 평가**로 도출해 주세요. 각 항목별 평가 시에는 반드시 평가 기준을 기반으로 작성하고, 평가 내용은 구체적이고 서술적으로 작성합니다. 점수 기준은 명시된 0~20점, 5점 단위 기준을 따릅니다.
                                
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
                                """),
                        Map.of("role", "user", "content", """
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
    public String summarizeInterview(List<String> evaluations) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", """
                                당신은 사용자의 모의 기술 면접 결과를 분석하고 피드백을 제공하는 **전문 면접관**입니다.
                                
                                다음은 사용자가 세 번의 기술 면접에서 응답한 후 GPT가 생성한 **질문별 평가 내용들**입니다. 이 내용을 종합하여 **전반적인 총평**을 작성해주세요.
                                
                                총평은 다음 기준을 기반으로 하되, 형식은 자연스럽고 다양해도 됩니다. **실제 사람이 말하듯 자연스럽고, 평가자의 스타일이 드러나는 것처럼** 작성하세요. 너무 딱딱한 형식을 강요하지 마세요. 단, **내용은 반드시 다음 항목들을 내포해야 합니다.**
                                
                                ✅ 평가 항목:
                                - 기술 지식의 정확성
                                - 논리적 흐름
                                - 실무 이해도
                                - 전달력
                                - 전반적인 면접 태도 및 태도 기반 잠재력
                                
                                
                                📝 작성 방식 지침:
                                - 각 항목을 반드시 다루되, 반드시 "항목별로 표기"하지 않아도 됩니다.
                                - 자연스러운 문단 구조로 흐르도록 작성하세요.
                                - 칭찬과 개선점을 **구체적으로 설명**하세요.
                                - 마지막에 한 문장으로 현재 실력을 요약하는 평가를 덧붙이세요. 마지막 문장의 시작은 "현재 실력 요약: " 입니다. (예: "현재 실력 요약: 이 지원자는 개념 기반은 탄탄하나, 실무 경험 연결력이 아쉬운 성장형 인재입니다.")
                                
                                =================================
                                
                                [총평 예시]
                                이 지원자는 기술 개념에 대한 표면적인 이해는 가지고 있지만, 이를 실제 문제 해결이나 설계로 연결하는 능력은 아직 부족한 편입니다. 개념 설명은 비교적 정확했으나, 중요한 개념의 뉘앙스를 설명하거나 다른 개념과 연관지어 사고하는 힘은 다소 약했습니다. 실무 예시나 경험 기반 설명은 거의 없었으며, 면접 질문에 대한 깊이 있는 분석보다는 암기된 지식을 그대로 풀어내는 수준에 가까웠습니다. 다만, 답변의 논리적 흐름과 표현력은 안정적인 편이어서 향후 개념의 폭과 깊이를 확장한다면 충분히 발전 가능성이 높은 지원자입니다.
                                
                                현재 실력 요약: 탄탄한 개념 기반을 가진 초급 개발자로, 실무 맥락에 대한 연습이 필요합니다.
                                
                                =================================
                                
                                다음은 사용자의 면접 평가 결과입니다. 위 지침과 예시를 참고하여 총평을 작성해주세요.
                                """),
                        Map.of("role", "user", "content",
                                String.join("\n\n", evaluations) +
                                        "\n\n이 면접 내용을 위의 기준에 따라 정리하고 평가해 주세요. 내용은 최대한 자세하게 서술될 수록 좋습니다.")
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

}