# emergency-room-backend

위치 기반 실시간 응급실 병상 정보를 제공하기 위한 백엔드 서비스입니다.

공공 응급의료 API를 수집해 병원/응급실 상태 데이터를 저장하고, PostGIS 기반 반경 조회와 Redis 캐시를 통해 주변 응급실 정보를 빠르게 제공하는 것을 목표로 합니다.

## 핵심 기능
- 전국 응급실 병원 정보 적재
- 응급실 상태 정보 적재 및 최신화
- 위치 기반 반경 내 병원 조회
- 병원 상세 및 상태 요약 조회
- 지역 기반 병원 조회 및 이름 검색
- 병원 상세 응답에 상태 라벨, 최근 갱신 여부, 연락처/좌표 보유 여부 제공

## 기술 스택
- Kotlin
- Spring Boot
- PostgreSQL / PostGIS
- Redis
- Flyway

## 검증 메모
- 실제 공공 API 초기 적재 확인
- 병원 데이터 531건, 응급실 상태 데이터 417건 적재 확인
- 상태 요약 API 및 주변 병원 조회 API 응답 확인
- 병원 상세 API 응답 확장 후 `./gradlew build` 통과

## 현재 구조
- `features.emergency.hospital`: 병원 정보 조회
- `features.emergency.status`: 응급실 상태 조회
- `features.emergency.sync`: 초기 적재 및 최신화

## API
- `GET /api/v1/emergency/hospitals/{hospitalId}`: 병원 상세 조회
  - 응답 필드: `emergencyStatusLabel`, `updatedRecently`, `hasLocation`, `contactAvailable`
- `GET /api/v1/emergency/hospitals/nearby?latitude=37.56&longitude=126.97&radius=5000`: 반경 내 병원 조회
- `GET /api/v1/emergency/hospitals/region?region=서울특별시 중구`: 지역 기반 병원 조회
- `GET /api/v1/emergency/hospitals/search?keyword=서울`: 이름 검색
- `GET /api/v1/emergency/status/{hospitalId}`: 특정 병원 응급실 상태 조회
- `GET /api/v1/emergency/status/summary`: 전체 요약 조회
- `POST /api/v1/emergency/hospitals/initialize`: 병원 초기 적재
- `POST /api/v1/emergency/status/initialize`: 상태 초기 적재
- `POST /api/v1/emergency/status/refresh`: 상태 최신화

## 로컬 실행
1. `docker compose up -d`
2. `export EMERGENCY_API_BASE_URL=...`
3. `export EMERGENCY_API_SERVICE_KEY=...`
4. `./gradlew bootRun`

## 현재 상태
- 공공 API 연동 클라이언트 연결 완료
- 공공 API 페이지네이션 및 JSON 응답 처리 반영
- PostGIS 기반 반경 조회 구현 완료
- 병원 상세/주변/검색/지역 조회 API 구현 완료
- 상태 단건/요약/초기 적재/최신화 API 구현 완료
- 상태 단건/요약 조회에 Redis 캐시 적용
- 프론트 상세 패널에서 바로 사용할 수 있도록 병원 상세 응답 고도화 완료

## 다음 단계
- 병원 검색/지역 조회를 프론트에 실제 연동
- 상태 최신화 스케줄링 또는 배치 구조 추가
- 테스트 코드 추가
