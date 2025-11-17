<h1 align="center"><a href="#" target="_blank">Web IDE Backend</a></h1>

<p align="center">
  <a href="#"><img alt="Build Status" src="https://img.shields.io/badge/build-passing-brightgreen.svg"/></a>
  <a href="#"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.9.25-blue?logo=kotlin"/></a>
  <a href="#"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-3.5.7-brightgreen?logo=springboot"/></a>
  <a href="#"><img alt="JDK" src="https://img.shields.io/badge/Java-17-orange?logo=openjdk"/></a>
  <a href="#"><img alt="Gradle" src="https://img.shields.io/badge/Gradle-Kotlin DSL-02303A?logo=gradle"/></a>
  <a href="#"><img alt="MariaDB" src="https://img.shields.io/badge/MariaDB-10.6+-003545?logo=mariadb"/></a>
  <a href="#"><img alt="License" src="https://img.shields.io/badge/License-MIT-yellow.svg"/></a>
</p>

<p align="center">
  <a href="#"><img alt="star" src="https://img.shields.io/github/stars/yourname/yourrepo.svg?label=Stars&style=social"/></a>
  <a href="#"><img alt="fork" src="https://img.shields.io/github/forks/yourname/yourrepo.svg?label=Fork&style=social"/></a>
  <a href="#"><img alt="watch" src="https://img.shields.io/github/watchers/yourname/yourrepo.svg?label=Watch&style=social"/></a>
</p>

### Web IDE Core
- 실시간 코드 편집
- 프로젝트/파일 기반 워크스페이스
- 언어별 Docker 기반 런타임
- compile/execute command 기반 코드 실행

---

### Docker 기반 Execution Sandbox

#### Auto-Scaling Container Pool
- 사용자 증가 시 자동 컨테이너 확장
- 부하 감소 시 컨테이너 자동 축소
- 좀비/유령 컨테이너 자동 정리(GC)
- 컨테이너별 자원 제한:
    - CPU Limit
    - Memory Limit
    - File Size Limit
    - Network Mbps 제한
    - Timeout 제어

#### Execution Lifecycle
1. 코드 빌드
2. 실행
3. Resource Logging
4. Timeout / Error 처리
5. 결과 반환

---

## Security

- JWT 기반 인증
- 이메일 인증 시스템
---

## Multi-Tier 사용자 시스템
TODO
- FREE / PRO / ENTERPRISE 등 요금제 구조
- Tier별 리소스 제어(프로젝트 수, 메모리, CPU, 실행 시간 등)
- Tier 변경 이력(user_tier_mapping) 관리
---

## ️ Database Schema 

MariaDB 기반, JPA 엔티티로 관리  

### 주요 테이블

| Table | 설명 |
|--------|------|
| `users` | 사용자 정보 |
| `projects` | 프로젝트 메타데이터 |
| `code_files` | 코드 파일 및 소스 |
| `containers` | Docker 컨테이너 풀 |
| `container_resource_limits` | 컨테이너 자원 제한 |
| `execution_histories` | 코드 실행 이력 |
| `resource_usage_logs` | CPU/Memory/Time 로그 |
| `supported_languages` | 언어별 실행 설정 |
| `user_tiers` | 요금제 정보 |
| `user_tier_mapping` | 사용자 ↔ 요금제 매핑 |

---

## System Architecture
```
               ┌───────────────────────────────┐
               │          Frontend IDE         │
               │          React+vite+ts        │
               └───────────────┬───────────────┘
                               │ REST / WebSocket
                               ▼
                ┌───────────────────────────────┐
                │  Web IDE Backend              │
                │ Spring Boot 3.5 / JPA / JWT   │
                └─────────────────┬─────────────┘
                                  │
             ┌────────────────────┼────────────────────┐
             │                    │                    │
             ▼                    ▼                    ▼
┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐
│ Container Pool    │   │ Execution Engine │   │ Resource Logger  │
│ Auto-Scaling/GC   │   │ Compile/Execute  │   │ CPU/Mem/Time Log │
└──────────────────┘   └──────────────────┘   └──────────────────┘
             ▲                    ▲                    ▲
             └──────────────┬─────┴─────────────┬─────┘
                            ▼                   ▼
                    ┌──────────────┐     ┌───────────────┐
                    │   MariaDB    │     │ Docker Engine │
                    └──────────────┘     └───────────────┘
```
---

## 🛠️ Tech Stack

### Backend
- Kotlin 1.9.x
- Spring Boot 3.5.x
- Spring Security
- Spring Data JPA
- MariaDB
- Hibernate
- Gradle Kotlin DSL

### DevOps
- Docker
- Docker SDK / CLI
- Auto-Scaling Container Pool
- 리소스 모니터링 시스템

---

## 📦 Project Setup

### Requirements
- Java 17
- Docker Installed
- MariaDB
- Gradle 8+

---

## ⚙️ Build & Run

### 1. 환경 변수 설정

```bash
cp .env.example .env
