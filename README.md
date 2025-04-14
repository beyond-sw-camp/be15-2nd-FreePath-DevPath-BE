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
        <summary><b>1. We are FreePath</b></summary>
        <ul>
          <li><a href="#team">🗺️ Team FreePath</a></li>
          <li><a href="#project">📹 Our Project</a></li>
        </ul>
      </details>
    </td>
  </tr>
  <tr>
    <td>
      <details open>
        <summary><b>2. Project Result</b></summary>
        <ul>
          <li><a href="#requirements">📣 요구사항 명세서</a></li>
          <li><a href="#ddd">📑 DDD</a></li>
          <li><a href="#erd">📋 논리/물리 ERD</a></li>
          <li><a href="#restapi">✔️ REST API 설계 문서</a></li>
          <li><a href="#msa">📌 MSA 아키텍쳐 구조도</a></li>
          <li><a href="#test">🎖️ 테스트 결과 보고서</a></li>
          <li><a href="#wbs">📝 WBS</a></li>
          <li><a href="#gantt">📊Gantt Chart</a></li>
        </ul>
      </details>
    </td>
  </tr>
  <tr>
    <td>
      <details open>
        <summary><b>3. Our Notion Page</b></summary>
        <ul>
          <li><a href="#ournotionpage">📓 Notion Page Link</a></li>
        </ul>
      </details>
    </td>
  </tr>
</table>

<br>

<h1>🗂️ We are FreePath</h1>

## <span id="team">🗺️ Team FreePath</span>

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
<br>
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white"> 
  <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
    <img src="https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white">
  <img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
<br>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
<br>
  <img src="https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white">
</div>

<br><br>

<h5>프로젝트 목적</h5>
프로젝트 <b>DevPath</b>는 비전공자와 주니어 개발자들이 실전 준비 과정에서 겪는 단절을 해소하고자 합니다. 구조화된 피드백과 동료와의 협업, 성장 추적 기능을 통해 지속적인 성장을 지원합니다. 이를 통해 단편적인 정보 소비를 넘어 자기 주도적 학습과 회고가 가능한 환경을 제공합니다.
<br><br>
<details>
<summary>프로젝트 구조</summary>

```angular2html
com.freepath.devpath
├── common
│   ├── auth
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   └── service
│   ├── config
│   ├── converter
│   ├── dto
│   ├── exception
│   ├── jwt
│   └── service
│
│
├── user # Users 
│   ├── command
│   ├── config
│   ├── exception
│   └── query
│
├── email # ITNews
│   ├── command
│   ├── config
│   ├── exception
│   └── query
│ 
├── chatting # Chatting, ChattingJoin, ChattingRoom, UserBlock
│   ├── command
│   ├── exception
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
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── repository
│   └── service
│
└── csquiz # CsQuiz, CsQuizResult, CsQuizOption
    ├── command
    ├── exception
    └── query

```
</details>
<br>

<h1 id="project-result">🗂️ 프로젝트 산출물</h1>

<br>
<h3 id="requirements">📣 요구사항 명세서</h3>
<div markdown="1">
    <a href="./assets/project-result/requirement.pdf" target="_blank"><strong>| 요구사항 명세서 보기 |</strong></a>
</div>

<br>
<h3 id="ddd">📑 DDD</h3>
<div markdown="1">
        <a href="https://www.notion.so/DDD-Miro-1c22fdb1414880e484dade87a24fbc40" target="_blank"><strong>| DDD 보기 |</strong></a>
</div>

<br>
<h3 id="erd">📋 논리/물리 ERD</h3>

<details>
    <summary><b>논리 ERD 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/project-result/BE-PJT-3team-erd(1).png" alt="논리 ERD" style="width: 100%; height: auto;">
        <br><br>
    </div>
</details>
<details>
    <summary><b>물리 ERD 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/project-result/BE-PJT-3team-erd(2).png" alt="물리 ERD" style="width: 100%; height: auto;">
    </div>
</details>



<h3 id="wbs">📝 WBS (Work Breakdown Structure)</h3>
<details>
    <summary><b>WBS 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/project-result/wbs.png" alt="WBS" style="width: 100%; height: auto;">
        <br>
        <a href="https://www.notion.so/WBS-1c82fdb141488047835cd0ac36875e59" target="_blank">
            <b>| WBS 상세 문서 보기 |</b>
        </a>
    </div>
</details>

<br>
<h3 id="gantt">📊 Gantt Chart</h3>
<details>
    <summary><b>Gantt Chart 상세보기</b></summary>
    <div markdown="1">
        <img src="./assets/project-result/ganttchart.png" alt="Gantt Chart" style="width: 100%; height: auto;">
        <br>
        <a href="https://www.notion.so/Gantt-Chart-1d52fdb14148807aba37f0dedc7945a6" target="_blank">
            <b>| Gantt chart 상세 문서 보기 |</b>
        </a>
    </div>
</details>

<br>
<h1 id="Notion Page Link">🗂️ Our Playground</h1>
<a href="https://www.notion.so/be15_1st_Project_DB-5c52900ed26b42ad812641a28fe85249"><text><strong>| 📓 Notion Page Link |</text></strong></a><br><br>

<img src="./assets/scrum_meeting.jpg" style="width: 100%; height: auto;">
<center><i>매일 아침 8시 40분에 진행하는 Scrum Meeting</i></center>


