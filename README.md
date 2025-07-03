## KiWee (임시)

- Spring boot + JPA 환경으로 RESTFul API 서버
----
### 공통 응답 객체
```json
{
  "isSuccess": "boolean",
  "message": "string",
  "data": "object"
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

#### 인증 (Authorization)
- Common `/api/v1/auth`

##### 재발급
- POST `/refresh` - 재발급
  + refreshToken(Required): string

##### 로그아웃
- POST `/logout` - 로그아웃

#### 채널 (Channel)
- Common `/api/v1/channel`

##### 생성
- POST - 채널 생성
  + name(Required): string
  + icon(Required): string
  + banner(Required): string
  + description: string
  + isOriginal(Required): boolean
  + isPublic(Required): boolean

- POST `/{id}/join` - 채널 가입
  + id: UUID

- POST `/{id}/members/{memberId}/roles` - 맴버 채널 역할 부여
  + id: UUID
  + memberId: UUID
  + roles: RoleType

##### 조회
- GET - 공개 및 가입 채널 조회
  + page: number
  + size: number

- GET `/all` - 모든 채널 조회
  + page: number
  + size: number

- GET `/{id}` - 채널 상세 조회
  + id: UUID

- GET `/{id}/members` - 채널 맴버 조회
  + id: UUID
  + page: number
  + size: number

##### 수정
- PATCH `/{id}` - 채널 수정
  + id: UUID
  + name: string
  + icon: string
  + banner: string
  + description: string
  + isPublic: boolean

- POST `/{id}/members/{memberId}/ben` - 맴버 채널 벤 관련
  + id: UUID
  + memberId: UUID
  + isBen: boolean

##### 삭제
- DELETE `/{id}` - 채널 삭제
  + id: UUID

- DELETE `/{id}/members{memberId}/roles/{roleType}` - 맴버 채널 역할 제거
  + id: UUID
  + memberId: UUID
  + roleType: roleType

- DELETE `/{id}/members/{memberId}` - 맴버 추방
  + id: UUID
  + memberId: UUID

#### 초대
- Common `/api/v1/invites`

##### 생성
- POST - 채널 초대
  + domain: DomainType
  + targetId: UUID
  + inviteeId: UUID
  + message: string
  + condition: InviteCondition
  + maxUses: number

##### 조회
- GET - 공개 초대코드 조회
  + page: number
  + size: number

- GET `/me` - 내게 온 초대 조회
  + page: number
  + size: number

- GET `/{id}` - 초대 상세 조회
  + id: UUID

- GET `/code/{code}` - 초대 코드 조회
  + code: string

#### 수정
- POST `/{code}/accept` - 초대 수락
  + code: string

- POST `/{code}/reject` - 초대 거절
  + code: string

- POST `/{code}/expired` - 초대권 무효화
  + code: string

- POST `/{code}/expired/force` - 초대권 강제 무효화
  + code: string

#### 알림 (Notification)
- Common `/api/v1/notifications`

##### 조회
- GET `/me` - 알림 목록 조회
  + includeRead: boolean
  + page: number
  + size: number

##### 수정

- GET `/me/{id}/read` - 알림 읽기
  + id: UUID

- GET `/me/{id}/unread` - 알림 안읽음 처리
  + id: UUID

##### 삭제
- DELETE `/me/{id}` - 알림 삭제
  + id: UUID
