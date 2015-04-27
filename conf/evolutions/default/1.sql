# Contacts schema

# --- !Ups

CREATE SEQUENCE id_seq;
CREATE TABLE Contacts (
    id integer NOT NULL DEFAULT nextval('id_seq'),
    name varchar(200),
    phone varchar(40)
);

# --- !Downs

DROP TABLE Contacts;
DROP SEQUENCE id_seq;