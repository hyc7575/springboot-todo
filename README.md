# Todo API

## 실행 방법

### 필요한 환경

- JDK 25
- 별도의 DB 설치는 필요하지 않습니다. 서버를 실행하면 H2 파일 DB가 `db_dev.mv.db`로 자동 생성됩니다.

### 실행 명령

macOS / Linux:

```bash
./gradlew bootRun
```

Windows:

```bat
gradlew.bat bootRun
```

서버 주소는 `http://localhost:8080`입니다.

## API 명세

### 할 일 생성

- 메서드: `POST`
- 주소: `/todo/create`
- 상태 코드: `201 Created`

요청 본문:

```json
{
  "title": "Spring 공부하기",
  "content": "JPA 복습"
}
```

응답 본문:

```json
{
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "content": "JPA 복습",
    "completed": false,
    "createdDateTime": "2026-09-21T12:00:00.123456",
    "status": "ACTIVE"
  }
}
```

제목은 필수이며 공백일 수 없고 최대 40자입니다.

### 할 일 목록 조회

- 메서드: `GET`
- 주소: `/todo/list`
- 요청 본문: 없음
- 상태 코드: `200 OK`

응답 본문:

```json
{
  "data": [
    {
      "id": 1,
      "title": "Spring 공부하기",
      "content": "JPA 복습",
      "completed": false,
      "createdDateTime": "2026-09-21T12:00:00.123456",
      "status": "ACTIVE"
    }
  ]
}
```

### 할 일 단건 조회

- 메서드: `GET`
- 주소: `/todo/{id}`
- 요청 본문: 없음
- 상태 코드: `200 OK`
- 없는 ID의 상태 코드: `404 Not Found`

응답 본문:

```json
{
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "content": "JPA 복습",
    "completed": false,
    "createdDateTime": "2026-09-21T12:00:00.123456",
    "status": "ACTIVE"
  }
}
```

### 할 일 수정

- 메서드: `PUT`
- 주소: `/todo/modify/{id}`
- 상태 코드: `200 OK`
- 없는 ID의 상태 코드: `404 Not Found`

요청 본문:

```json
{
  "title": "Spring 공부하기",
  "content": "JPA 복습 완료",
  "completed": true,
  "status": "ACTIVE"
}
```

응답 본문:

```json
{
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "content": "JPA 복습 완료",
    "completed": true,
    "createdDateTime": "2026-09-21T12:00:00.123456",
    "status": "ACTIVE"
  }
}
```

### 할 일 삭제

- 메서드: `DELETE`
- 주소: `/todo/{id}`
- 요청 본문: 없음
- 상태 코드: `200 OK`
- 없는 ID의 상태 코드: `404 Not Found`

응답 본문:

```json
{
  "data": {
    "id": 1,
    "title": "Spring 공부하기",
    "content": "JPA 복습 완료",
    "completed": true,
    "createdDateTime": "2026-09-21T12:00:00.123456",
    "status": "DELETED"
  }
}
```

## 설계 설명

- 모든 주소를 `/todo`로 시작하여 할 일 API라는 것을 구분했습니다. 생성·목록·수정은 각각 `/create`, `/list`, `/modify/{id}`로 기능을 명시하고, 단건 조회와 삭제는 `{id}`로 대상을 지정했습니다.
- 생성 성공은 새로운 데이터가 만들어졌으므로 `201 Created`를 사용합니다. 조회와 수정은 처리 결과를 응답하므로 `200 OK`를 사용합니다. 삭제도 삭제 상태로 변경된 결과를 응답하므로 `204 No Content` 대신 `200 OK`를 사용합니다.
- 요청 값이 잘못되면 클라이언트가 요청을 고칠 수 있도록 `400 Bad Request`를 사용합니다. 대상 ID가 없으면 리소스를 찾을 수 없다는 의미의 `404 Not Found`를 사용합니다.
- 별도의 설치 없이 바로 실행할 수 있고 파일에 데이터를 보존할 수 있어 H2 파일 DB를 사용했습니다. 데이터 접근은 Spring Data JPA로 분리하여 다른 관계형 DB로 변경하기 쉽게 구성했습니다.
- 삭제는 DB 행을 제거하지 않고 상태를 `DELETED`로 바꾸는 소프트 삭제 방식이며, 삭제된 데이터는 조회 결과에서 제외합니다.

## 실행 결과

아래 예시는 ID가 `1`로 생성된 경우입니다.

### 1. 만들기

요청:

```bash
curl -i -X POST http://localhost:8080/todo/create \
  -H 'Content-Type: application/json' \
  -d '{"title":"Spring 공부하기","content":"JPA 복습"}'
```

응답:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{"data":{"id":1,"title":"Spring 공부하기","content":"JPA 복습","completed":false,"createdDateTime":"2026-09-21T12:00:00.123456","status":"ACTIVE"}}
```

### 2. 목록 조회

요청:

```bash
curl -i http://localhost:8080/todo/list
```

응답:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"data":[{"id":1,"title":"Spring 공부하기","content":"JPA 복습","completed":false,"createdDateTime":"2026-09-21T12:00:00.123456","status":"ACTIVE"}]}
```

### 3. 완료 처리

요청:

```bash
curl -i -X PUT http://localhost:8080/todo/modify/1 \
  -H 'Content-Type: application/json' \
  -d '{"title":"Spring 공부하기","content":"JPA 복습","completed":true,"status":"ACTIVE"}'
```

응답:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"data":{"id":1,"title":"Spring 공부하기","content":"JPA 복습","completed":true,"createdDateTime":"2026-09-21T12:00:00.123456","status":"ACTIVE"}}
```

### 4. 삭제

요청:

```bash
curl -i -X DELETE http://localhost:8080/todo/1
```

응답:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"data":{"id":1,"title":"Spring 공부하기","content":"JPA 복습","completed":true,"createdDateTime":"2026-09-21T12:00:00.123456","status":"DELETED"}}
```

### 5. 잘못된 제목 요청

요청:

```bash
curl -i -X POST http://localhost:8080/todo/create \
  -H 'Content-Type: application/json' \
  -d '{"title":"   ","content":"내용"}'
```

응답:

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{"code":"INVALID_ARGUMENT","message":"제목은 필수입니다."}
```

### 6. 없는 ID 조회

요청:

```bash
curl -i http://localhost:8080/todo/999999
```

응답:

```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{"code":"TODO_NOT_FOUND","message":"id가 존재하지 않습니다."}
```
