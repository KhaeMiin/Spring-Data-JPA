#### 메소드명으로 쿼리 생성
[Spring Data JPA 공식문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)\
- 주의: 필드명이 변경되면 인터페이스에서 정의한 메서드의 이름도 함꼐 변경해야함.
<br>그렇지 않으면 애플리케이션 실행 시점에서 오류 발생 (로딩 시점에 오류가 발생한다는 것이 큰 장점)

<br>

- 단점: 조건이 추가될 떄 메소드명이 더 길어진다. (조건이 2개가 넘어가면 다른 방법으로 접근하자)