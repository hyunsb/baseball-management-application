# ⚾ 토이프로젝트2 - 야구 관리 프로그램 ⚾

Java + MySQL + JDBC

### 역할 분담
- `정현수` <br>
  ◻ 프로젝트 베이스코드 구현 (Console, RequestParser, Main 메서드) <br>
  ◻ 야구장(Stadium) 관련 기능 구현(등록, 전체목록) <br>
  ◻ 팀(Team) 관련 기능 구현(등록, 전체목록) <br>

- `배종윤` <br>
  ◻ 선수(Player) 관련 기능 구현(등록, 팀별 선수 목록) <br> 
  ◻ 퇴출선수(Out_Player) 관련 기능 구현(등록, 전체 목록) <br>
  ◻ 포지션별 팀 야구 선수페이지 구현 <br>
  ◻ ResponseDTO 출력 구현
    - 각 ResponseDTO를 전달 받아 동적으로 class 의field name과 values를<br>
      추출해 table 형식으로 출력
    - 포지션별 팀 야구 선수페이지
      - ResponseDTO를 전달 받아 pivot형식으로 출력
