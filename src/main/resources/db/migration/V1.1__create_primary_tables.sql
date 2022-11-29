drop table if exists user_credentials;
create table user_credentials (
    id          bigint not null auto_increment,
    username    varchar(64) unique not null,
    password    varchar(64) not null,
    ACCOUNT_EXPIRED boolean,
    ACCOUNT_LOCKED boolean,
    CREDENTIALS_EXPIRED boolean,
    ENABLED boolean,
    primary key(id)
) engine=InnoDB;

drop table if exists custom_users;
create table custom_users (
    id                  bigint not null auto_increment,
    user_id             varchar(64) unique not null,
    first_name          varchar(64) not null,
    last_name           varchar(64),
    email               varchar(64) not null,
    user_credential_id  bigint not null,
    primary key(id)
) engine=InnoDB;


drop table if exists user_authority;
create table user_authority (
    id                  bigint not null auto_increment,
    name                varchar(64) unique not null,
    primary key(id)
) engine=InnoDB;

drop table if exists user_authorities;
create table user_authorities
(
    user_id                  bigint       not null,
    authority_id             bigint       not null,
    KEY (user_id),
    KEY (authority_id)
) engine = InnoDB;

insert into user_credentials(username, PASSWORD, ACCOUNT_EXPIRED, ACCOUNT_LOCKED, CREDENTIALS_EXPIRED, ENABLED)
    values('admin@gmail.com','$2a$08$OttNZyoF8Iil8BqpKp8aGemXC48ww9ULtmjY.bVw9bFfQjW7y9tIu', FALSE, FALSE, FALSE, TRUE);

insert into user_authority(id, name) values (1, "ADMIN");
insert into user_authority(id, name) values (2, "USER");

insert into user_authorities(user_id, authority_id) values(1,1);
insert into user_authorities(user_id, authority_id) values(1,2);
