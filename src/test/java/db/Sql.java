package db;

public enum Sql {
    PLAYER("create table player (\n" +
            "    id bigint primary key auto_increment,\n" +
            "    name varchar(20) not null,\n" +
            "    position varchar(20) not null,\n" +
            "    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,\n" +
            "    team_id bigint null,\n" +
            "    foreign key (team_id) references team(id),\n" +
            "\t\tunique key (position, team_id) #다중 유니크 제약\n" +
            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;", "DROP TABLE if exists player"),

    OUT_PLAYER("create table out_player (\n" +
           "    id bigint primary key auto_increment,\n" +
           "    reason varchar(45) unique not null,\n" +
           "    player_id bigint,\n" +
           "    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,\n" +
           "    foreign key (player_id) references player(id)\n" +
           "    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;", "DROP TABLE if exists out_player");

    private final String create;
    private final String drop;

    Sql(String create, String drop) {
        this.create = create;
        this.drop = drop;
    }

    public String getCreate() {
        return create;
    }

    public String getDrop() {
        return drop;
    }
}
