insert into stadium (name) values ("1경기장");
insert into stadium (name) values ("2경기장");
insert into stadium (name) values ("3경기장");

insert into team (stadium_id, name) values (1, "NC");
insert into team (stadium_id, name) values (2, "롯데");
insert into team (stadium_id, name) values (3, "한화");

-- Insert players for Team 1
INSERT INTO player (name, position, team_id) VALUES
  ('한승엽', '투수', 1),
  ('이병규', '포수', 1),
  ('박찬호', '1루수', 1),
  ('류현진', '2루수', 1),
  ('김광현', '3루수', 1),
  ('나성범', '좌익수', 1),
  ('추신수', '중견수', 1),
  ('정훈', '유격수', 1),
  ('강정호', '우익수', 1);

-- Insert players for Team 2
INSERT INTO player (name, position, team_id) VALUES
  ('류건희', '투수', 2),
  ('강민호', '포수', 2),
  ('이대호', '1루수', 2),
  ('최형우', '2루수', 2),
  ('김하성', '3루수', 2),
  ('이정후', '좌익수', 2),
  ('박건우', '중견수', 2),
  ('오지환', '유격수', 2),
  ('손아섭', '우익수', 2);

-- Insert players for Team 3
INSERT INTO player (name, position, team_id) VALUES
  ('류현진', '투수', 3),
  ('손아섭', '포수', 3),
  ('박병호', '1루수', 3),
  ('김현수', '2루수', 3),
  ('이정후', '3루수', 3),
  ('최형우', '좌익수', 3),
  ('박건우', '중견수', 3),
  ('나성범', '유격수', 3),
  ('김하성', '우익수', 3);
