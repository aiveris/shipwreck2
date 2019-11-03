CREATE TABLE shipwreck
(
    id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name varchar(255),
    description varchar(2000),
    conditionn varchar(255),
    depth int,
    latitude double,
    longitude double,
    year_discovered int
);
CREATE UNIQUE INDEX shipwreck_id_uindex ON shipwreck (id);