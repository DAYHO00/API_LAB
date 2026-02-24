# API Performance Lab (Spring Boot)

같은 기능의 API를 구현 방식별로 나눠 성능을 수치로 비교하는 실험형 프로젝트입니다.  
(N+1 → JOIN 최적화 → Index → Redis Cache → Load Test)

---

## 목차

- [프로젝트 개요](#-프로젝트-개요)
- [실험 시나리오](#-실험-시나리오)
- [기술 스택](#-기술-스택)
- [데이터 모델](#-데이터-모델)
- [아키텍처](#-아키텍처)
- [실험 단계](#-실험-단계)
- [실행 방법](#-실행-방법)
- [측정 결과](#-측정-결과)

---

## 🎯 프로젝트 개요

동일한 API를 여러 방식으로 구현하고,  
응답 시간 / 쿼리 수 / 실행 계획(EXPLAIN) / 반복 측정 결과를 비교합니다.

이 프로젝트는 다음 질문에 답하기 위해 설계되었습니다:

- N+1 문제는 실제로 얼마나 느려지는가?
- fetch join은 얼마나 개선되는가?
- 인덱스는 실행 계획에 어떤 영향을 주는가?
- Redis 캐시는 어느 정도까지 성능을 끌어올리는가?
- 반복 호출 시 어떤 구조가 가장 안정적인가?

---

## 🧪 실험 시나리오

- 데이터 구성
  - Project 100개
  - Project당 Task 50개
  - 총 Task 5,000개
- 동일한 응답을 다음 방식으로 구현하여 비교
  - V1: N+1 발생
  - V2: Fetch Join
  - V3: 인덱스 검증
  - V4: Redis Cache
  - V5: 반복 측정

---

## ⚙️ 기술 스택

- Spring Boot
- Spring Data JPA
- MySQL 8
- Redis
- Docker Compose
- PowerShell Benchmark Script

---

## 🗂 데이터 모델

```
Project (1) ─── (N) Task
```

### Project

- id
- name

### Task

- id
- title
- project_id (FK)

---

## 🏗 아키텍처

```
Controller
   ↓
Service (버전별 구현)
   ↓
Repository (JPA)
   ↓
MySQL

(V4)
Service
   ↓
Redis Cache
   ↓
MySQL
```

---

## 🚀 실험 단계

### V1: 단순 조회 (N+1 유도)

- Project 조회
- 각 Project의 Task Lazy Loading
- 프로젝트 수만큼 추가 쿼리 발생

---

### V2: JOIN 최적화 (fetch join)

- fetch join 사용
- 단일 쿼리로 Project + Task 조회
- N+1 제거

---

### V3: 인덱스 적용 (EXPLAIN 비교)

```sql
EXPLAIN SELECT * FROM task WHERE project_id = 1;
```

확인 결과:

- type = ref
- key = FK 인덱스
- rows ≈ 50

→ 인덱스가 실제로 사용됨을 확인

---

### V4: Redis 캐시 적용

- `@Cacheable` 사용
- 첫 요청: DB 조회 + 캐시 저장 (Cache Miss)
- 이후 요청: Redis Hit

---

### V5: 반복 측정

- 워밍업 제외
- 동일 조건 반복 호출
- 평균값 비교

---

## 🏃 실행 방법

### 1️⃣ Docker 실행

```bash
docker compose up -d
```

- MySQL: localhost:3307
- Redis: localhost:6379

---

### 2️⃣ Spring Boot 실행

```bash
cd api
gradlew bootRun
```

---

### 3️⃣ 테스트 URL

#### N+1

```
GET http://localhost:8080/experiments/nplus1/projects-task-count
```

#### Fetch Join

```
GET http://localhost:8080/experiments/join/projects-task-count
```

#### Redis Cache

```
GET http://localhost:8080/experiments/cache/projects-task-count
```

---

## 📊 측정 결과

### 🔹 N+1

```
308ms
378ms
315ms
280ms
260ms
```

평균 ≈ **308ms**

- 프로젝트 수만큼 쿼리 반복
- 다수의 DB round-trip 발생

---

### 🔹 Fetch Join

```
207ms
99ms
70ms
75ms
82ms
88ms
```

평균 ≈ **103ms**

- 단일 JOIN 쿼리
- N+1 대비 약 3배 개선

---

### 🔹 Redis Cache (Hit 기준)

```
1.43s   ← 첫 요청 (miss)
27ms
12ms
13ms
14ms
```

Cache Hit 평균 ≈ **16ms**

- DB 미접근
- JOIN 대비 약 6~7배 개선

---

## 📌 최종 비교 요약

| 방식        | 평균 응답 시간 |
| ----------- | -------------- |
| N+1         | ~308ms         |
| Fetch Join  | ~103ms         |
| Redis Cache | ~16ms          |

---
