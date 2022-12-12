create table gift_certificate
(
    id               int auto_increment
        primary key,
    name             varchar(45)                               not null,
    description      varchar(300)                              not null,
    price            decimal(8, 2)                             not null,
    duration         int                                       not null,
    create_date      timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    last_update_date timestamp(3) default CURRENT_TIMESTAMP(3) not null on update CURRENT_TIMESTAMP(3)
);

create table tag
(
    id   int auto_increment
        primary key,
    name varchar(45) not null,
    constraint name_UNIQUE
        unique (name)
);

create table gift_certificate_has_tag
(
    gift_certificate_id int not null,
    tag_id              int not null,
    primary key (gift_certificate_id, tag_id),
    constraint fk_gift_certificate_has_tag_gift_certificate
        foreign key (gift_certificate_id) references gift_certificate (id)
            on update cascade on delete cascade,
    constraint fk_gift_certificate_has_tag_tag1
        foreign key (tag_id) references tag (id)
            on update cascade on delete cascade
);

create index fk_gift_certificate_has_tag_gift_certificate_idx
    on gift_certificate_tags (gift_certificate_id);

create index fk_gift_certificate_has_tag_tag1_idx
    on gift_certificate_tags (tag_id);

create table user
(
    id   int auto_increment
        primary key,
    name varchar(45) not null,
    constraint name_UNIQUE
        unique (name)
);

create table `order`
(
    id          int auto_increment
        primary key,
    user_id     int                                       not null,
    create_date timestamp(3) default CURRENT_TIMESTAMP(3) not null,
    total       decimal(8, 2)                             not null,
    constraint fk_order_copy2_user1
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

create index fk_order_copy2_user1_idx
    on `order` (user_id);

create table order_has_gift_certificate
(
    order_id            int not null,
    gift_certificate_id int not null,
    quantity            int not null,
    primary key (order_id, gift_certificate_id),
    constraint fk_order_has_gift_certificate_gift_certificate1
        foreign key (gift_certificate_id) references gift_certificate (id)
            on update cascade on delete cascade,
    constraint fk_order_has_gift_certificate_order1
        foreign key (order_id) references `order` (id)
            on update cascade on delete cascade
);

create index fk_order_has_gift_certificate_gift_certificate1_idx
    on gift_certificate_orders (gift_certificate_id);

create index fk_order_has_gift_certificate_order1_idx
    on gift_certificate_orders (order_id);

