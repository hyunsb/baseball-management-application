#경기장
create table stadium (
    id bigint primary key auto_increment,
    name varchar(45) unique not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

#팀
create table team (
    id bigint primary key auto_increment,
    name varchar(45) unique not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    stadium_id bigint,
    foreign key (stadium_id) references stadium(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

#선수
create table player (
    id bigint primary key auto_increment,
    name varchar(20) not null,
    position varchar(20) not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    team_id bigint null,
    foreign key (team_id) references team(id),
		unique key (position, team_id) #다중 유니크 제약
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

#퇴출선수
create table out_player (
    id bigint primary key auto_increment,
    reason varchar(45) unique not null,
    player_id bigint,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    foreign key (player_id) references player(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;
