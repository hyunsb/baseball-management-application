# ⚾ 토이프로젝트2 - 야구 관리 프로그램 ⚾

Java + MySQL + JDBC

## ⚙ 기능
- 야구장 등록
- 전체 야구장 목록보기
- 팀 등록
- 전체 팀 등록
- 선수 등록
- 팀별 선수 목록보기
- 선수 퇴출 등록
- 선수 퇴출 목록보기
- 포지션별 팀 야구 선수 페이지

  
## 🙍‍♂️역할 분담
- ### 정현수
  ◻ 프로젝트 베이스코드 구현 (Console, RequestParser, Main 메서드) <br>
  &nbsp;&nbsp;&nbsp; - `Main`: 어노테이션과 리플렉션을 통해 요청과 실행 메서드를 매핑하도록 구현 <br>
  &nbsp;&nbsp;&nbsp; - `RequestParser`: 쿼리스트링 요청 형식 검증을 진행하고 각 요청에 세부작인 데이터 검증은 해당 객체에서 진행하도록 구현 <br>
  &nbsp;&nbsp;&nbsp; - `Console`: Scanner를 싱글톤으로 관리하고 애플리케이션에 필요한 메서드만 노출시키도록 구현 <br>


  ◻ 야구장(Stadium) 관련 기능 구현(등록, 전체목록) <br>
  ◻ 팀(Team) 관련 기능 구현(등록, 전체목록) <br>

- ### 배종윤
  ◻ 선수(Player) 관련 기능 구현(등록, 팀별 선수 목록) <br> 
  ◻ 퇴출선수(Out_Player) 관련 기능 구현(등록, 전체 목록) <br>
  ◻ 포지션별 팀 야구 선수페이지 구현 <br>
  ◻ ResponseDTO 출력 구현
    - 각 ResponseDTO를 전달 받아 동적으로 class 의field name과 values를<br>
      추출해 table 형식으로 출력
    - 포지션별 팀 야구 선수페이지
      - ResponseDTO를 전달 받아 pivot형식으로 출력
