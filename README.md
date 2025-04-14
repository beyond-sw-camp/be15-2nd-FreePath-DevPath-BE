<div align="center">

![header](https://capsule-render.vercel.app/api?type=blur&color=0:D9E5FF,100:0054FF&height=200&section=header&text=We%20are%20DevPath!&fontSize=70)

<h1>개발자를 위한 커뮤니티가 여러분을 찾아갑니다</h1>
<h3>개발 능력 업그레이드를 DevPath와 함께!</h3>

</div>
<br><br>
<h1>🔍 목차</h1>

<table>
  <tr>
    <td>
      <details open>
        <summary><b>1. We are FreePath 👋</b></summary>
        <ul>
          <li><a href="#team">Team FreePath 🪄</a></li>
          <li><a href="#project">Our Project 📹</a></li>
        </ul>
      </details>
    </td>
  </tr>
  <tr>
    <td>
      <details open>
        <summary><b>2. Project Result 🗂️</b></summary>
        <ul>
          <li><a href="#wbs">WBS 📝</a></li>
          <li><a href="#gantt">Gantt Chart 📊</a></li>
          <li><a href="#requirements">요구사항 명세서 📣</a></li>
          <li><a href="#erd">논리/물리 ERD 📋</a></li>
          <li><a href="#restapi">REST API 설계 문서 ✔️</a></li>
          <li><a href="#msa">MSA 아키텍쳐 구조도 📌</a></li>
          <li><a href="#test">테스트 결과 보고서 🎖️</a></li>
        </ul>
      </details>
    </td>
  </tr>
  <tr>
    <td>
      <details open>
        <summary><b>3. Our Notion Page 🗂️</b></summary>
        <ul>
          <li><a href="#ournotionpage">Notion Page Link 📓</a></li>
        </ul>
      </details>
    </td>
  </tr>
</table>

<br>

## <span id="team">🪄 Team FreePath</span>

<div align="center">

|                                **김운경**                                |                                **김태인**                                |                                **이기연**                                |
| :----------------------------------------------------------------------: | :----------------------------------------------------------------------: | :----------------------------------------------------------------------: |
| <img src="./assets/profile/김운경.png" width="180" height="180"> | <img src="./assets/profile/김태인.png" width="180" height="180"> | <img src="./assets/profile/이기연.png" width="180" height="180"> |
|                [@splguyjr](https://github.com/splguyjr)                |              [@Taein5415](https://github.com/Taein5415)                  |                  [@Lee-gi-yeun](https://github.com/Lee-gi-yeun)                  |

|                                **이주미**                                |                               **이채은**                               |                                **하채린**                                |
| :----------------------------------------------------------------------: |:-------------------------------------------------------------------:| :----------------------------------------------------------------------: |
| <img src="./assets/profile/이주미.png" width="180" height="180"> | <img src="./assets/profile/이채은.png" width="180" height="180"> | <img src="./assets/profile/하채린.png" width="180" height="180"> |
|                 [@z00m-1n](https://github.com/z00m-1n)                 |              [@nineeko](https://github.com/nineeko)               |                   [@didiha](https://github.com/didiha)                   | 

</div>

<br><br>

## <span id="project">📹 Our Project</span>

### <span id="tech-stack">💻 기술 스택</span>

<div>
  <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
  <img src="https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white">
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white">  
<br>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
<br>
  <img src="https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</div>

<br><br>

<h5>프로젝트 목적</h5>
프로젝트 <b>DevPath</b>는 비전공자와 주니어 개발자들이 실전 준비 과정에서 겪는 단절을 해소하고자 합니다. 구조화된 피드백과 동료와의 협업, 성장 추적 기능을 통해 지속적인 성장을 지원합니다. 이를 통해 단편적인 정보 소비를 넘어 자기 주도적 학습과 회고가 가능한 환경을 제공합니다.
<br><br>
<details>
<summary>프로젝트 구조</summary>

```angular2html
com.devpath
├── common
│   ├── auth
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   └── service
│   ├── config
│   ├── jwt
│   └── response
│
├── config
│
├── exception
│   └── (예외 처리 클래스들)
│
├── user # Users 
│   ├── command
│   └── query
│
├── itnews # ITNews
│   ├── command
│   └── query
│ 
├── chatting # Chatting, ChattingJoin, ChattingRoom, UserBlock
│   ├── command
│   └── query
│
├── board
│   ├── post # Board, BoardCategory, Attachment
│   │   ├── command
│   │   └── query
│   │   
│   ├── comment # Comment
│   │   ├── command
│   │   └── query
│   │   
│   ├── vote # Vote, VoteItem, VoteHistory
│   │   ├── command
│   │   └── query
│   │   
│   └── interaction # Like, BoardBookmark
│       ├── command
│       └── query
│
├── interview # Interview, InterviewRoom
│   ├── command
│   └── query
│
├── report # Report, ReportCheck
│   ├── command
│   └── query
│
└── csquiz # CsQuiz, CsQuizResult, CsQuizOption
    ├── command
    └── query

```
</details>
<br>

<h1 id="project-result">🗂️ 프로젝트 산출물</h1>

<h3 id="wbs">📝 WBS (Work Breakdown Structure)</h3>
<details>
    <summary><b>WBS 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/wbs.png" alt="WBS" style="width: 80%; height: auto;">
        <br>
        <a href="https://www.notion.so/WBS-1972fdb1414880e5927bfca57c78818e" target="_blank">
            <b>🔗 WBS 상세 문서 보기</b>
        </a>
    </div>
</details>

<h3 id="gantt">📊 Gantt Chart</h3>
<details>
    <summary><b>Gantt Chart 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/gantt.png" alt="Gantt Chart" style="width: 80%; height: auto;">
    </div>
</details>

<h3 id="requirements">📣 요구사항 명세서</h3>
<details>
    <summary><b>요구사항 명세서 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/요구사항명세서.png" alt="요구사항 명세서" style="width: 80%; height: auto;">
    </div>
</details>

<h3 id="table-spec">📋 테이블 명세서</h3>

<details>
    <summary><b>테이블 명세서 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/table_spec.png" alt="테이블 명세서" style="width: 80%; height: auto;">
    </div>
</details>

<h3 id="usecase">👤 Usecase</h3>
<details>
    <summary>Usecase 자세히 보기</summary>
    <div markdown="1">
        <img src="./assets/usecase.png" alt="usecase" style="max-width: 100%; height: auto;">    
    </div>
</details>

<h3 id="erd">📌 ERD</h3>
<details>
    <summary>ERD Cloud 자세히 보기</summary>
    <div markdown="1">
        <img src="./assets/erdcloud.png" alt="ERD Diagram" style="max-width: 100%; height: auto;">
    </div>
</details>

```mermaid
erDiagram

    authority {
        INT authority_id PK
        VARCHAR authority_name
    }

    category {
        BIGINT category_id PK
        VARCHAR category_name
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    user {
        VARCHAR user_id PK
        INT authority_id FK
        VARCHAR password
        VARCHAR contact_number
        VARCHAR email
        INT age
        VARCHAR gender
        VARCHAR profile_image_url
        VARCHAR is_alarm_enabled
        VARCHAR is_consent_provided
        VARCHAR account_status
        INT reported_count
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
        FLOAT remaining_point
    }

    store {
        BIGINT store_id PK
        VARCHAR user_id FK
        BIGINT category_id FK
        VARCHAR business_registration_number
        VARCHAR business_operation_certificate_url
        VARCHAR store_name
        VARCHAR contact_number
        VARCHAR address
        VARCHAR address_detail
        VARCHAR business_hours
        FLOAT average_rating
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    notification_type {
        BIGINT notification_type_id PK
        VARCHAR notification_message
    }

    report_type {
        BIGINT report_type_id PK
        VARCHAR name
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    card_company {
        BIGINT card_company_id PK
        VARCHAR card_company_name
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    receipt {
        BIGINT receipt_id PK
        VARCHAR user_id FK
        BIGINT store_id FK
        BIGINT card_company_id FK
        VARCHAR receipt_body
        INT amount
        VARCHAR payment_method
        VARCHAR transaction_status
        VARCHAR is_canceled
        TIMESTAMP created_at
        TIMESTAMP deleted_at
    }

    review {
        BIGINT review_id PK
        VARCHAR user_id FK
        BIGINT store_id FK
        VARCHAR content
        INT rating
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    comment {
        BIGINT comment_id PK
        VARCHAR user_id FK
        BIGINT review_id FK
        VARCHAR content
        TIMESTAMP created_at
        TIMESTAMP modified_at
        TIMESTAMP deleted_at
    }

    review_like {
        BIGINT like_id PK
        BIGINT review_id FK
        VARCHAR user_id FK
        TIMESTAMP created_at
    }

    review_image {
        BIGINT review_image_id PK
        BIGINT review_id FK
        VARCHAR review_image_url
    }

    point {
        BIGINT point_id PK
        VARCHAR user_id FK
        BIGINT reference_point_id FK
        BIGINT receipt_id FK
        VARCHAR transaction_type
        INT point
        TIMESTAMP created_at
        VARCHAR is_canceled
    }

    point_exchange_history {
        BIGINT point_exchange_id PK
        VARCHAR user_id FK
        BIGINT point_product_id FK
        INT quantity
        TIMESTAMP created_at
    }

    favorite {
        BIGINT favorite_id PK
        VARCHAR user_id FK
        BIGINT store_id FK
        TIMESTAMP created_at
        TIMESTAMP deleted_at
    }

    store_image {
        BIGINT store_image_id PK
        BIGINT store_id FK
        VARCHAR store_image_url
    }

    login_history {
        BIGINT login_history_id PK
        VARCHAR user_id FK
        TIMESTAMP login_at
        VARCHAR ip_address
        VARCHAR device_type
    }

    report {
        BIGINT report_id PK
        BIGINT report_type_id FK
        VARCHAR user_id FK
        BIGINT comment_id FK
        BIGINT review_id FK
        VARCHAR report_comment
        TIMESTAMP created_at
    }

    penalty_history {
        BIGINT penalty_history_id PK
        VARCHAR user_id FK
        VARCHAR admin_id FK
        VARCHAR penalty_reason
        TIMESTAMP start_penalty_at
        TIMESTAMP end_penalty_at
    }

    notification_history {
        BIGINT notification_history_id PK
        BIGINT notification_type_id FK
        VARCHAR user_id FK
        TIMESTAMP read_at
        TIMESTAMP created_at
    }

    authority ||--|{ user : has
    category ||--|{ store : has
    user ||--|{ store : owns
    user ||--|{ receipt : makes
    user ||--|{ review : writes
    user ||--|{ comment : writes
    user ||--|{ review_like : likes
    user ||--|{ point : earns
    user ||--|{ point_exchange_history : exchanges
    user ||--|{ favorite : marks
    user ||--|{ login_history : logs
    user ||--|{ report : reports
    user ||--|{ penalty_history : penalized
    user ||--|{ notification_history : receives
    store ||--|{ review : has
    store ||--|{ favorite : liked
    store ||--|{ store_image : has
    store ||--|{ receipt : generates
    review ||--|{ comment : has
    review ||--|{ review_like : liked
    review ||--|{ review_image : has
    review ||--|{ report : reported
    comment ||--|{ report : reported
    receipt ||--|{ point : generates
    receipt ||--|{ card_company : paid_with
    report_type ||--|{ report : classified
    notification_type ||--|{ notification_history : has

```

<i><center><u>mermaid로 표현한 ERD</u></center></i>

<h3 id="process-flow">🎖️ 핵심 로직 플로우차트</h3>

<h4>1. 영수증 발행 프로세스</h4>
프로시저 세부 다이어그램은 <code>./src/mobile-receipt/point/</code> 경로의 다어어그램 참조
<br><br>

```mermaid
flowchart TD
    A[시작: SP_ISSUE_RECEIPT] --> B[입력값 검증]
    B --> C[사용자 검증<br/>SP_VALIDATE_USER]

    C --> D[중복 거래 검증<br/>SP_VALIDATE_DUPLICATE_RECEIPT]
    D --> E{중복 거래?}
    E -->|Yes| F[에러: 중복 거래]
    E -->|No| G[START TRANSACTION]

    G --> H[영수증 발행<br/>RECEIPT 테이블]
    H --> I[포인트 적립<br/>SP_POINT_EARN]
    I --> J[COMMIT]
    J --> K[종료]

    %% 에러 처리
    F --> L[에러 발생]
    H -->|실패| M[ROLLBACK]
    I -->|실패| M
    M --> L

```

<h4>2. 영수증 취소 프로세스</h4>
프로시저 세부 다이어그램은 <code>./src/mobile-receipt/point/</code> 경로의 다어어그램 참조
<br><br>

```mermaid
flowchart TD
    A[시작: SP_CANCEL_RECEIPT] --> B[원본 영수증 검증<br/>SP_VALIDATE_RECEIPT]
    B --> C[사용자 검증<br/>SP_VALIDATE_USER]

    C --> D[원본 포인트 내역 조회]
    D --> E{포인트 내역 존재?}
    E -->|No| F[에러: 포인트 내역 없음]
    E -->|Yes| G[START TRANSACTION]

    G --> H[원본 영수증 취소 처리<br/>RECEIPT 테이블]
    H --> I[포인트 취소<br/>SP_POINT_CANCEL]
    I --> J[COMMIT]
    J --> K[종료]

    %% 에러 처리
    F --> L[에러 발생]
    H -->|실패| M[ROLLBACK]
    I -->|실패| M
    M --> L

```

<br>

<h3 id="test-cases">🧪 테스트 케이스</h3>

<h4>일반 테스트</h4>
<details>
    <summary><b>고객 유형 분석 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/고객유형분석.gif" alt="고객 유형 분석 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>매장 매출 분석 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/매장매출분석.gif" alt="매장 매출 분석 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>베스트 리뷰 선정 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/베스트리뷰선정.gif" alt="베스트 리뷰 선정 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>베스트 리뷰 알림 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/베스트리뷰알림.gif" alt="베스트 리뷰 알림 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>시간대별 분석 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/시간대별분석.gif" alt="시간대별 분석 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>영수증 발행 알림 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/영수증발행알림.gif" alt="영수증 발행 알림 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>요일별 분석 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/etc/요일별분석.gif" alt="요일별 분석 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<h4>영수증 관련 테스트</h4>
<details>
    <summary><b>초기 데이터 확인 및 프로시저 등록 확인 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/01_초기데이터 확인, 프로시저 등록 확인.gif" alt="초기 데이터 확인 및 프로시저 등록 확인 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>영수증 발행 및 포인트 적립 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/02_영수증 발행, 포인트 적립.gif" alt="영수증 발행 및 포인트 적립 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>포인트 물품 교환(사용) 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/03_포인트 물품 교환(사용).gif" alt="포인트 물품 교환(사용) 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>영수증 취소 및 포인트 적립 취소 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/04_영수증 취소, 포인트 적립 취소.gif" alt="영수증 취소 및 포인트 적립 취소 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>에러 테스트 - 잔여 포인트 초과 물품 구매 시도 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/05_에러_잔여 포인트 초과 물품 구매 시도.gif" alt="에러 테스트 - 잔여 포인트 초과 물품 구매 시도 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>에러 테스트 - 재고 초과 수량 구매 시도 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/06_에러_재고 초과 수량 구매 시도.gif" alt="에러 테스트 - 재고 초과 수량 구매 시도 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>에러 테스트 - 이미 취소된 영수증 취소 재시도 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/07_이미 취소된 영수증 취소 재시도.gif" alt="에러 테스트 - 이미 취소된 영수증 취소 재시도 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>에러 테스트 - 10초 이내 중복 결제 시도 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/08_10초 이내 중복 결제 시도.gif" alt="에러 테스트 - 10초 이내 중복 결제 시도 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<details>
    <summary><b>결과 데이터 확인 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/receipt/09_결과 데이터 확인.gif" alt="결과 데이터 확인 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>
<details>
    <summary><b>레플리케이션 서버 테스트 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/test_case_gif/replication-server/레플리케이션서버.gif" alt="레플리케이션 서버 테스트 데모" style="max-width: 100%; height: auto;">    
    </div>
</details>

<br>
<br>
<h1 id="Our-Playground">🗂️ Our Playground</h1>
<a href="https://www.notion.so/be15_1st_Project_DB-5c52900ed26b42ad812641a28fe85249"><text><strong>| 📓 Notion Page Link |</text></strong></a><br><br>

<img src="./assets/scrum_meeting.jpg" style="width: 100%; height: auto;">
<center><i>매일 아침 8시 40분에 진행하는 Scrum Meeting</i></center>


