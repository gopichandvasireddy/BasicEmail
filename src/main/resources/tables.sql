
drop table mail_map;

drop table mail;

drop table users;

create table users (
id int NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    name varchar(255),
    PRIMARY KEY (ID)
);

create table mail (
id int NOT NULL AUTO_INCREMENT,
    body longtext,
    subject text,
    author varchar(255),
    PRIMARY KEY (ID)
);

create table mail_map (
id int NOT NULL AUTO_INCREMENT,
    mail_id int,
    user_id int,
    sender_id int,
    category ENUM ('INBOX', 'DRAFT', 'TRASH'),
    is_read boolean default false,
    PRIMARY KEY (ID),
    FOREIGN KEY (mail_id) REFERENCES mail(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);