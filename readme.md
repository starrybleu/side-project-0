## API 환경 구성

- java 1.8
- spring boot 2.1.x
- jpa (ORM)
- jwt
- lombok
- swagger 2.9.x

## 실행 가이드

### 공통
- jdk 1.8 설치 
- `JAVA_HOME` 환경 변수를 jdk 가 설치된 디렉토리로 설정

### windows

- `gradlew.bat :clean :bootRun`
- `http://localhost:8080/swagger-ui.html` 접속 후 API 실행 가능

### mac || linux

- `./gradlew.sh :clean :bootRun`
- `http://localhost:8080/swagger-ui.html` 접속 후 API 실행 가능