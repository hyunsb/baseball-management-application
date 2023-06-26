package db;

public enum Sql {
    PLAYER("create table player (\n" +
            "    id bigint primary key auto_increment,\n" +
            "    name varchar(20) not null,\n" +
            "    position varchar(20) not null,\n" +
            "    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,\n" +
            "    team_id bigint,\n" +
            "    foreign key (team_id) references team(id),\n" +
            "\t\tunique key (position, team_id) #다중 유니크 제약\n" +
            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;", "DROP TABLE if exists player");

    private String create;
    private String drop;

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
