# ⚾ 토이프로젝트2 - 야구 관리 프로그램 ⚾

- 개발 환경: Java + MySQL + JDBC <br>
- 프로젝트 기간: 2023.06 (1 week)

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

### 정현수
- 프로젝트 베이스코드 구현 (Console, RequestParser, Main 메서드)
  - `Main`: 어노테이션과 리플렉션을 통해 요청과 실행 메서드를 매핑하도록 구현
  - `RequestParser`: 쿼리스트링 요청 형식 검증을 진행하고 각 요청에 세부작인 데이터 검증은 해당 객체에서 진행하도록 구현
  - `Console`: Scanner를 싱글톤으로 관리하고 애플리케이션에 필요한 메서드만 노출시키도록 구현
- 야구장(Stadium) 관련 기능 구현(등록, 전체목록)
- 팀(Team) 관련 기능 구현(등록, 전체목록)

### 배종윤
- 선수(Player) 관련 기능 구현(등록, 팀별 선수 목록) <br> 
- 퇴출선수(Out_Player) 관련 기능 구현(등록, 전체 목록) <br>
- 포지션별 팀 야구 선수페이지 구현 <br>
- ResponseDTO 출력 구현
  - 각 ResponseDTO를 전달 받아 동적으로 class 의field name과 values를 추출해 table 형식으로 출력
  - 포지션별 팀 야구 선수페이지
      - ResponseDTO를 전달 받아 pivot형식으로 출력
   
## DB설정
mysql
  Set in [DBConnectInfo.java](https://github.com/hyunsb/baseball-management-application/blob/main/src/main/java/db/DBConnectInfo.java)
  - user : {username}
  - password : {password}, default - 1234

  table 생성
  - table query : [table.sql](https://github.com/hyunsb/baseball-management-application/blob/main/src/test/java/db/table.sql)
  - data query for help test : [data.sql](https://github.com/hyunsb/baseball-management-application/blob/main/src/test/java/db/data.sql)
  - warnning : team_id, stadium_id는 auto_inc 이기 때문에 임의로 데이터 삽입시 제대로 연결되지 않음
    

# DOCS

## 야구장 등록

**Request:**
```
야구장등록?name={name}
```

**Parameters:**
- `name` (string, required): 야구장 이름

**Response:**
```
StadiumResponse에 담아 출력합니다.

--------------------------------------
| id | name  | createAt              |
--------------------------------------
| 1  | 잠실야구장 | 2023-06-30 12:10:35.0 |
--------------------------------------
```

## 전체 야구장 목록

**Request:**
```
야구장목록
```

**Response:**
```
List<StadiumResponse>에 담아 출력합니다.

---------------------------------------
| id | name   | createAt              |
---------------------------------------
| 1  | 잠실야구장  | 2023-06-30 12:20:54.0 |
| 2  | 사직경기장  | 2023-06-30 12:21:19.0 |
| 3  | 창원NC파크 | 2023-06-30 12:21:41.0 |
---------------------------------------
```

## 팀 등록

**Request:**
```
팀등록?stadiumId={statiumId}&name={name}
```

**Parameters:**
- `stadiumId` (int, required) : 경기장 아이디
- `name` (string, required): 팀 이름

**Response:**
```
TeamResponse에 담아 출력합니다.

-------------------------------------------------
| id | name | stadiumId | createAt              |
-------------------------------------------------
| 1  | nc   | 1         | 2023-06-30 12:23:12.0 |
-------------------------------------------------
```

## 전체 팀 목록

**Request:**
```
팀목록
```

**Response:**
```
List<TeamWithStadiumResponse>에 담아 출력합니다.

-----------------------------------------------------------------------------------------------
| teamId | teamName | teamCreatedAt         | stadiumId | stadiumName | stadiumCreatedAt      |
-----------------------------------------------------------------------------------------------
| 1      | nc       | 2023-06-30 12:23:12.0 | 1         | 잠실야구장       | 2023-06-30 12:20:54.0 |
| 2      | 롯데       | 2023-06-30 12:23:50.0 | 2         | 사직경기장       | 2023-06-30 12:21:19.0 |
| 3      | 한화       | 2023-06-30 12:23:55.0 | 3         | 창원NC파크      | 2023-06-30 12:21:41.0 |
-----------------------------------------------------------------------------------------------
```

## 선수 등록

**Request:**
```
선수등록?teamId={id}&name={name}&position={position}
```

**Parameters:**
- `teamId` (int, required) : 팀 아이디
- `name` (string, required): 선수 이름
- `position` (string, required): 포지션

**Response:**
```
PlayerDTO.FindPlayerResponse에 담아 출력합니다.

------------------------
| id | name | position |
------------------------
| 1  | 한승엽  | 투수   |
------------------------
```

## 팀별 선수 목록

**Request:**
```
선수목록?teamId={teamId}
```

**Parameters:**
- `teamId` (int, required) : 팀 아이디

**Response:**
```
List<PlayerDTO.FindPlayerResponse>에 담아 출력합니다.

------------------------
| id | name | position |
------------------------
| 1  | 이병규  | 포수   |
| 2  | 박찬호  | 1루수  |
| 3  | 류현진  | 2루수  |
| 4  | 김광현  | 3루수  |
| 5  | 나성범  | 좌익수 |
| 6  | 추신수  | 중견수 |
| 7  | 정훈   | 유격수  |
| 8  | 강정호  | 우익수 |
------------------------
```

## 선수 퇴출 등록

**Request:**
```
퇴출등록?playerId={playerId}&reason={reason}
```

**Parameters:**
- `playerId` (int, required) : 선수 아이디
- `reason` (string, required): 퇴출 사유

**Response:**
```
OutPlayerDTO.FindOutPlayerResponse에 담아 출력합니다.
```

## 선수 퇴출 목록

**Request:**
```
퇴출목록
```

**Response:**
```
List<OutPlayerDTO.FindOutPlayerResponse>에 담아서 출력합니다.

---------------------------------------------------------------
| playerId | name   | position   | reason   | outDay                |
---------------------------------------------------------------
| 1        | 이병규  | 포수       | 도박     | 2023-06-30 12:30:49.0 |
---------------------------------------------------------------
```

## 포지션별 팀 야구 선수 페이지

**Request:**
```
포지션별목록
```

**Response:**
```
List<PlayerDTO.findPlayerGroupByPosition> 값을 담아서 콘솔에 출력합니다.

teamName | 한화 | nc  | 롯데
--------------------------
우익수      김하성   강정호   손아섭
3루수      이정후   김광현   김하성
2루수      김현수   류현진   최형우
중견수      박건우   추신수   박건우
1루수      박병호   박찬호   이대호
좌익수      최형우   나성범   이정후
투수       류현진         류건희
포수       손아섭         강민호
유격수      나성범   정훈   오지환
```
