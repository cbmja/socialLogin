# [ 자격증 기출문제 온라인 풀이 서비스 ]
## URL : <a href="http://spocbt.cafe24.com/exam/list" target="_blank"> spocbt </a>

<br/>

### ![icons8-썸네일-보기](https://github.com/user-attachments/assets/06048a41-ac4a-4ac6-9294-33c71a7fb568) 목차  

- 프로젝트 소개
- 기술 스택
- ERD
- API 명세
- 설명
- 소감

---

<br/>

### ![icons8-연필](https://github.com/user-attachments/assets/4e5e85df-cf09-4afe-ab60-02b572ee64e7) 1. 프로젝트 소개

[ spocbt ]는 1년에 약 45,000명이 응시하는 '생활스포츠지도사' 시험의 온라인 문제 풀이 사이트입니다.
무료 온라인 필기 시험 기능이 제공되며 체육인들의 정보 공유를 위한 커뮤니티 서비스를 제공합니다.

---

<br/>

### ![icons8-설정-3](https://github.com/user-attachments/assets/3b5f83d7-a26e-4aa9-83fe-6b7204af69d3) 2. 기술 스택


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

### ![icons8-압박-붕대](https://github.com/user-attachments/assets/b335475f-c4b3-43cd-ac2d-1eedac163e59) 3. ERD

![화면 캡처 2025-01-25 124236](https://github.com/user-attachments/assets/158a21fe-2929-46eb-b28d-e8d0badbd49b)

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

### ![icons8-문서](https://github.com/user-attachments/assets/b316ca03-a738-4304-81d3-89391e71a89b) 4. API 명세

![resized_image](https://github.com/user-attachments/assets/91d301d7-861d-4b5a-ac86-7ba03b193398)

---

<br/>

### ![icons8-시스템-작업](https://github.com/user-attachments/assets/cb69de7e-c678-434e-8084-68c0d98387de) 5. 설명

### 응시 가능 시험 목록

https://github.com/user-attachments/assets/75a5813f-3f5d-45ad-b357-965de76ac26b

<br/>

### 시험 선택 후 응시 과목 선택 모달

https://github.com/user-attachments/assets/ced6d405-3973-481b-a6b5-88c33683376a

<br/>

### 시험 응시 화면

https://github.com/user-attachments/assets/f1dc6f49-383f-452d-add9-70a4c4d0bd02

<br/>

### 시험 채점

https://github.com/user-attachments/assets/5e2e08ba-a14c-4371-a766-0e3378e0d9d1

---

<br/>

### <img src="https://github.com/user-attachments/assets/dd4852de-39e0-4265-98c2-fcb4b271eddb" alt="free-animated-icon-review" width="48" height="48"> 6. 소감

<br/>

빠른 구현에 초점을 맞춰 프로젝트를 진행을 해서인지 DB 설계와 API 설계에서 아쉬운 부분이 많이 있습니다.

해당 프로젝트는 조금더 발전시켜서 서비스를 해보고 싶다는 생각이 있기 때문에 일단 restful 하게 api를 수정하고,
시험 문제를 하나하나 추출하여 문제은행식으로 관리해 보려고 합니다.

현재 응시하는 시험지의 제작 과정은 아래와 같습니다.
1. 공식적으로 제공되는 pdf 시험지를 html로 변환
2. html 파일 내부의 선택지에 data 속성을 통해 어떤 과목의 몇 번 문제의 몇 번 선택지인지 구분할 수 있도록 함.

사실 pdf 시험지를 응시 가능한 html 시험지로 변환하는 과정이 제일 큰 고비였습니다.
이는 구글링과 chat gpt를 활용하여 python으로 작업하였습니다.






