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
