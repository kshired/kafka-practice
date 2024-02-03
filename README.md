# Kafka의 동작 원리를 알아보기 위한 예제

## Structure

이 예제는 kotlin 기반의 멀티 모듈 구조로 되어있으며, 각 모듈의 역할은 다음과 같음

### Producer
- kafka producer의 역할과 동작을 확인하기 위한 예제

### Consumer
- kafka consumer의 역할과 동작을 확인하기 위한 예제

### Common
- 다른 모듈에서 공통으로 사용하는 클래스를 정의한 모듈

### Practice
- Producer, Consumer 실전 예제
    - 파일로부터 변경사항을 읽어 postgresql DB에 데이터를 저장하는 예제

### Spring:api
- Spring boot 기반의 kafka producer 예제

### Spring:consumer
- Spring boot 기반의 kafka consumer 예제

### Spring:kafka
- Spring boot 기반의 kafka 공통 설정 모듈
