# MoneyLog 프로젝트 가이드

이 문서는 MoneyLog 가계부 애플리케이션의 개발자/팀원들을 위한 가이드 문서입니다.
프로젝트의 구조, 모듈별 역할, 실행 방법 등을 설명합니다.

## 1. 프로젝트 개요 (Overview)
MoneyLog는 사용자의 수입/지출 내역을 기록하고, 차트를 통해 시각적인 통계를 제공하는 가계부 애플리케이션입니다.

- **아키텍처**: Multi-Module Layered Architecture (MSA 준비 단계)
- **백엔드**: Spring Boot 3.4
- **프론트엔드**: Vue.js 3 + Vite + Chart.js

## 2. 프로젝트 구조 (Module Structure)
이 프로젝트는 Gradle 멀티 모듈로 구성되어 있습니다. 각 모듈은 명확한 역할과 책임을 가집니다.

```text
moneyLog/
├── build.gradle           # 루트 빌드 설정
├── common/                # [공통 모듈] 모든 모듈에서 사용하는 공통 유틸리티, DTO
├── user/                  # [유저 모듈] 회원 정보 및 인증 관리
├── money/                 # [머니 모듈] 가계부 핵심 로직, 통계 계산
├── api/                   # [API 모듈] 애플리케이션 진입점, 모듈 통합
└── frontend/              # [프론트엔드] Vue.js 웹 애플리케이션
```

### 각 모듈 설명
1. **common**:
   - `ApiResponse`: API 응답 표준 포맷을 정의합니다.
   - 다른 도메인 로직을 포함하지 않는 순수 유틸리티 성격의 모듈입니다.

2. **user**:
   - `Member`: 회원 엔티티(Entity)를 관리합니다.
   - 현재는 기본적인 회원 정보 구조만 잡혀 있으며, 추후 인증/인가(JWT 등) 로직이 추가될 예정입니다.

3. **money**:
   - `MoneyLog`: 수입/지출 내역 엔티티입니다.
   - `StatisticsService`: 주간/월간 및 시간대별 통계를 계산하는 핵심 비즈니스 로직이 있습니다.
   - `StatisticsController`: 통계 데이터를 프론트엔드에 제공하는 REST API입니다.

4. **api**:
   - `MoneyLogApplication`: Spring Boot 실행 메인 클래스가 위치합니다.
   - `user`, `money` 모듈을 의존하여 하나의 애플리케이션으로 실행시킵니다.

## 3. 개발 환경 설정 (Setup)
### 필수 요구 사항
- JDK 17 이상
- Node.js 18 이상

### 백엔드 실행
루트 디렉토리에서 다음 명령어를 실행합니다.
```bash
# 맥/리눅스
./gradlew :api:bootRun

# 윈도우
gradlew.bat :api:bootRun
```
- 서버는 `http://localhost:8080` 에서 실행됩니다.

### 프론트엔드 실행
`frontend` 디렉토리로 이동하여 실행합니다.
```bash
cd frontend
npm install # 최초 1회 의존성 설치
npm run dev
```
- 웹 화면은 `http://localhost:5173` 에서 확인할 수 있습니다.

## 4. 주요 기능 및 API
### 통계 기능
- **시간대별 통계**: 사용자의 지출 내역을 시간대(0시~23시)별로 합산하여 보여줍니다.
- **REST API**: `GET /api/stats/hourly`
- **화면**: 프론트엔드의 `/stats` 경로에서 도넛 차트로 확인할 수 있습니다.

## 5. 주의사항 for 팀원
- **Lombok 미사용**: 현재 JDK 25 호환성 문제 및 명시적인 코드 관리를 위해 Lombok을 사용하지 않고 Getter/생성자를 직접 구현했습니다. 엔티티 수정 시 주의해주세요.
- **모듈 의존성**: `api` 모듈은 `user`, `money`를 참조하지만, `user`와 `money`는 서로 느슨하게 결합되어야 합니다. (현재는 `money` -> `user` 단방향 참조 허용)
