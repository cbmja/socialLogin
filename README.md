# [ 자격증 기출문제 온라인 풀이 서비스 ]
## URL : <a href="http://spocbt.cafe24.com/exam/list" target="_blank"> spocbt </a>

<br/>

### ![icons8-썸네일-보기](https://github.com/user-attachments/assets/06048a41-ac4a-4ac6-9294-33c71a7fb568) 목차  

- 프로젝트 소개
- 기술 스택
- ERD
- API 명세
- 주요 기능
- 자체 피드백백

---

<br/>

### ![icons8-연필](https://github.com/user-attachments/assets/4e5e85df-cf09-4afe-ab60-02b572ee64e7) 프로젝트 소개

[ spocbt ]는 1년에 약 45,000명이 응시하는 '생활스포츠지도사' 시험의 온라인 문제 풀이 사이트입니다.
무료 온라인 필기 시험 기능이 제공되며 체육인들의 정보 공유를 위한 커뮤니티 서비스를 제공합니다.

---

<br/>

### ![icons8-설정-3](https://github.com/user-attachments/assets/3b5f83d7-a26e-4aa9-83fe-6b7204af69d3) 기술 스택


>__language__
- ![icons8-자바-커피-컵-로고-48](https://github.com/user-attachments/assets/02ef5592-484a-4d6d-b042-2aeeb2a8e8e7) java17 &nbsp;&nbsp; ![icons8-자바-스크립트-48](https://github.com/user-attachments/assets/88394d65-dc3a-4705-8e96-313cf27e533e) javascript &nbsp;&nbsp; ![icons8-html-48](https://github.com/user-attachments/assets/7bdfe914-9157-469a-bdf1-9bb047e90d88) html5 &nbsp;&nbsp; ![icons8-css3-48](https://github.com/user-attachments/assets/8a33fb8d-3687-4ecf-b116-5386b094fdf8) css3

<br/>

>__framework__
- ![icons8-봄-로고-48](https://github.com/user-attachments/assets/8e207415-7d5b-46ad-9368-1063e249107d) spring boot

<br/>

>__database__
- ![icons8-mysql-48](https://github.com/user-attachments/assets/148c0145-c45f-4e55-97e9-e18ee4880953) mysql

<br/>

>__util__
- ![icons8-intellij-idea-48](https://github.com/user-attachments/assets/abdb4127-d741-4611-86ac-6b3674d05799) intelliJ

---

<br/>

### ![icons8-압박-붕대](https://github.com/user-attachments/assets/b335475f-c4b3-43cd-ac2d-1eedac163e59) ERD
[erd.pdf](https://github.com/user-attachments/files/18574922/erd.pdf)

- member : 회원정보
- exam : 시험 정보 (2급 생활스포츠지도사 [ 자격증 기출문제 온라인 풀이 서비스 ]
## URL : <a href="http://spocbt.cafe24.com/exam/list" target="_blank"> spocbt </a>

<br/>

### ![icons8-썸네일-보기](https://github.com/user-attachments/assets/06048a41-ac4a-4ac6-9294-33c71a7fb568) 목차  

- 프로젝트 소개
- 기술 스택
- ERD
- API 명세
- 주요 기능

---

<br/>

### ![icons8-연필](https://github.com/user-attachments/assets/4e5e85df-cf09-4afe-ab60-02b572ee64e7) 프로젝트 소개

[ spocbt ]는 1년에 약 45,000명이 응시하는 '생활스포츠지도사' 시험의 온라인 문제 풀이 사이트입니다.
무료 온라인 필기 시험 기능이 제공되며 체육인들의 정보 공유를 위한 커뮤니티 서비스를 제공합니다.

---

<br/>

### ![icons8-설정-3](https://github.com/user-attachments/assets/3b5f83d7-a26e-4aa9-83fe-6b7204af69d3) 기술 스택


>__language__
- ![icons8-자바-커피-컵-로고-48](https://github.com/user-attachments/assets/02ef5592-484a-4d6d-b042-2aeeb2a8e8e7) java17 &nbsp;&nbsp; ![icons8-자바-스크립트-48](https://github.com/user-attachments/assets/88394d65-dc3a-4705-8e96-313cf27e533e) javascript &nbsp;&nbsp; ![icons8-html-48](https://github.com/user-attachments/assets/7bdfe914-9157-469a-bdf1-9bb047e90d88) html5 &nbsp;&nbsp; ![icons8-css3-48](https://github.com/user-attachments/assets/8a33fb8d-3687-4ecf-b116-5386b094fdf8) css3

<br/>

>__framework__
- ![icons8-봄-로고-48](https://github.com/user-attachments/assets/8e207415-7d5b-46ad-9368-1063e249107d) spring boot

<br/>

>__database__
- ![icons8-mysql-48](https://github.com/user-attachments/assets/148c0145-c45f-4e55-97e9-e18ee4880953) mysql

<br/>

>__util__
- ![icons8-intellij-idea-48](https://github.com/user-attachments/assets/abdb4127-d741-4611-86ac-6b3674d05799) intelliJ

---

<br/>

### ![icons8-압박-붕대](https://github.com/user-attachments/assets/b335475f-c4b3-43cd-ac2d-1eedac163e59) ERD

![화면 캡처 2025-01-25 124236](https://github.com/user-attachments/assets/df7bacfb-d991-41a5-836f-68fee1be2b65)



> 총 21개의 과목 중 시험별로 응시해야 하는 과목이 다름.

> 1.선택과목(7 中 택 4) + 필수과목(1) / 2.선택과목(7 中 택 5) / 3.필수과목(8) 총 세가지 타입으로 분류

- member : 회원정보
- exam : 시험 정보 (2급 생활스포츠지도사, 노인 스포츠지도사, 등 총 9개 시험)
- subject : 과목(21가지)
- exam_subject : 시험별 응시해야 하는 과목
- update_exam : db에 등록된 응시 가능 시험 정보
- answer_master : 시험 과목별 답안
- test_record : 시험 응시정보
- test_subject_record : 응시 시험 상세 정보(응시한 시험의 과목 정보)
- board_detail : 게시글
- comment : 댓글 

---

<br/>

### ![icons8-문서](https://github.com/user-attachments/assets/b316ca03-a738-4304-81d3-89391e71a89b) API 명세
![11111_0001](https://github.com/user-attachments/assets/6493b55e-fb4a-4238-b46c-64e4d5118b61)

---

<br/>

### ![icons8-시스템-작업](https://github.com/user-attachments/assets/cb69de7e-c678-434e-8084-68c0d98387de) 주요 기능

---

<br/>

### <img src="https://github.com/user-attachments/assets/dd4852de-39e0-4265-98c2-fcb4b271eddb" alt="free-animated-icon-review" width="48" height="48"> 자체 피드백






