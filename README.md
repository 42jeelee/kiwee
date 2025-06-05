## KiWee (임시)

- Spring boot + JPA 환경으로 RESTFul API 서버
----
### 공통 응답 객체
```json
{
  "isSuccess": boolean,
  "message": string,
  "data": object
}
```
----
### API EndPoints

#### 맴버 (MEMBER)
- Common `/api/v1/members`

##### 조회
- GET - 맴버 검색
  + keyword: string
  + page: number
  + size: number

- GET `/{id}` - 맴버 상세조회

- GET `/me` - 내 정보 조회

##### 생성
- POST - 맴버 생성
  + name(Required): string
  + nickname(Required): string
  + email: string
  + imageUrl: string

##### 수정
- PATCH `/{id}` - 맴버 수정
  + name: string
  + nickname: string
  + email: string
  + imageUrl: string

- PATCH `/{id}/exp` - 맴버 경험치 획득
  + exp(Required): number

##### 삭제
- DELETE `/{id}` - 맴버 삭제

#### 인기도 (Reputation)
- Common `/api/v1/reputations`

##### 조회
- GET `/ranking` - 월 랭킹 조회
  + yearMonth: 'yyyy-MM'
  + page: number
  + size: number

- GET `/{id}` - 월 맴버 투표현황
  + yearMonth: 'yyyy-MM'

- GET `/{id}/logs` - 맴버 투표기록
  + page: number
  + size: number

##### 생성
- POST - 인기도 투표
  + giverId(Required): UUID
  + receiverId(Required): UUID
  + isUp: boolean

#### 플랫폼 (Platform)
- Common `/api/v1/platforms`

##### 조회
- GET - 모든 플랫폼 조회
  + keyword: string
  + page: number
  + size: number

- GET `/{id}` - 플랫폼 상세 조회

##### 생성
- POST - 플랫폼 생성
  + name(Required): string
  + icon(Required): string
  + banner(Required): string
  + description: string
  + page: string

##### 수정
- PATCH `/{id}` - 플랫폼 수정
  + name: string
  + icon: string
  + banner: string
  + description: string
  + page: string

##### 삭제
- DELETE `/{id}` - 플랫폼 삭제

#### 인증 (AUTHORIZATION)
- Common `/api/v1/auth`

##### 재발급
- POST `/refresh` - 재발급
  + refreshToken(Required): string

##### 로그아웃
- POST `/logout` - 로그아웃
