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
- [회고](#-회고)

---

## 🎯 프로젝트 개요

동일한 API를 여러 방식으로 구현하고, 응답 시간/TPS/쿼리 수/실행 계획(EXPLAIN)을 비교합니다.

---

## 🧪 실험 시나리오

- 대상 API: `GET /api/projects/{id}/dashboard`
- 포함 데이터: 프로젝트 정보, 멤버 목록, 최근 작업(Task), 간단 통계

---

## ⚙️ 기술 스택

- Spring Boot, Spring Data JPA
- MySQL, EXPLAIN
- Redis (Cache)
- JMeter (Load Test)
- Docker Compose

---

## 🗂 데이터 모델

- Project (1) - (N) Task
- Member (1) - (N) Task(assignee)
- Project (N) - (M) Member via ProjectMember

---

## 🏗 아키텍처

- Controller → Service(버전별 구현) → Repository → MySQL
- (M4) Redis Cache 적용

---

## 🚀 실험 단계

- V1: 단순 조회 (N+1 유도)
- V2: JOIN 최적화 (fetch join / DTO projection)
- V3: 인덱스 적용 (EXPLAIN 비교)
- V4: Redis 캐시 적용
- V5: JMeter 부하 테스트

---

## 🏃 실행 방법

(여기에 docker-compose / bootRun / curl 예시)

---

## 📊 측정 결과

(여기에 표/그래프/스크린샷)

---

## 🔍 회고

(무엇을 배웠는지, 실무 적용 포인트)
