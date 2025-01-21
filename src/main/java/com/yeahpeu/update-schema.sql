CREATE TABLE budget
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    wedding_id   BIGINT NOT NULL,
    total_budget BIGINT NOT NULL,
    used_budget  BIGINT NOT NULL,
    CONSTRAINT pk_budget PRIMARY KEY (id)
);

CREATE TABLE category
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    name        VARCHAR(255) NOT NULL,
    category_id BIGINT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE category_expense
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    wedding_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    expense     INT    NOT NULL,
    CONSTRAINT pk_category_expense PRIMARY KEY (id)
);

CREATE TABLE chat_message
(
    message_id        BIGINT       NOT NULL,
    created_at        datetime NULL,
    updated_at        datetime NULL,
    room_id           BIGINT       NOT NULL,
    text              VARCHAR(255) NOT NULL,
    parent_message_id BIGINT NULL,
    sender_id         BIGINT       NOT NULL,
    sent_at           datetime     NOT NULL,
    CONSTRAINT pk_chat_message PRIMARY KEY (message_id, room_id)
);

CREATE TABLE chat_message_content
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    content_type VARCHAR(50)  NOT NULL,
    url          VARCHAR(255) NOT NULL,
    message_id   BIGINT NULL,
    room_id      BIGINT NULL,
    CONSTRAINT pk_chat_message_content PRIMARY KEY (id)
);

CREATE TABLE chat_room
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    created_at              datetime NULL,
    updated_at              datetime NULL,
    title                   VARCHAR(255) NOT NULL,
    reservered_member_count INT          NOT NULL,
    used_member_count       INT          NOT NULL,
    CONSTRAINT pk_chat_room PRIMARY KEY (id)
);

CREATE TABLE checklist
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    schedule_id  BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    is_completed BIT(1)       NOT NULL,
    CONSTRAINT pk_checklist PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE schedule
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    updated_at       datetime NULL,
    wedding_id       BIGINT       NOT NULL,
    title            VARCHAR(255) NOT NULL,
    date             datetime     NOT NULL,
    location         VARCHAR(255) NULL,
    main_category_id BIGINT NULL,
    sub_category_id  BIGINT NULL,
    price            VARCHAR(255) NULL,
    is_completed     BIT(1)       NOT NULL,
    CONSTRAINT pk_schedule PRIMARY KEY (id)
);

CREATE TABLE user_category
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    user_id     BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT pk_user_category PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    dtype                VARCHAR(31) NULL,
    email_address        VARCHAR(255) NOT NULL,
    password             VARCHAR(255) NOT NULL,
    name                 VARCHAR(255) NULL,
    nickname             VARCHAR(255) NULL,
    provider             VARCHAR(255) NULL,
    provider_id          VARCHAR(255) NULL,
    user_id              BIGINT       NOT NULL,
    chat_room_id         BIGINT       NOT NULL,
    last_read_message_id BIGINT       NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE wedding
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    bride_id     BIGINT   NOT NULL,
    groom_id     BIGINT   NOT NULL,
    wedding_day  datetime NOT NULL,
    is_onboarded BIT(1)   NOT NULL,
    CONSTRAINT pk_wedding PRIMARY KEY (id)
);

CREATE TABLE wish_item
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    wishlist_id BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    image_url   VARCHAR(255) NULL,
    price       INT          NOT NULL,
    link_url    VARCHAR(255) NOT NULL,
    maker       VARCHAR(255) NULL,
    CONSTRAINT pk_wish_item PRIMARY KEY (id)
);

CREATE TABLE wishlist
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    wedding_id BIGINT NOT NULL,
    CONSTRAINT pk_wishlist PRIMARY KEY (id)
);

ALTER TABLE budget
    ADD CONSTRAINT uc_budget_wedding UNIQUE (wedding_id);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_role UNIQUE (`role`);

ALTER TABLE wedding
    ADD CONSTRAINT uc_wedding_bride UNIQUE (bride_id);

ALTER TABLE wedding
    ADD CONSTRAINT uc_wedding_groom UNIQUE (groom_id);

ALTER TABLE wishlist
    ADD CONSTRAINT uc_wishlist_wedding UNIQUE (wedding_id);

ALTER TABLE budget
    ADD CONSTRAINT FK_BUDGET_ON_WEDDING FOREIGN KEY (wedding_id) REFERENCES wedding (id);

ALTER TABLE category_expense
    ADD CONSTRAINT FK_CATEGORY_EXPENSE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE category_expense
    ADD CONSTRAINT FK_CATEGORY_EXPENSE_ON_WEDDING FOREIGN KEY (wedding_id) REFERENCES wedding (id);

ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE chat_message_content
    ADD CONSTRAINT FK_CHAT_MESSAGE_CONTENT_ON_MEIDROID FOREIGN KEY (message_id, room_id) REFERENCES chat_message (message_id, room_id);

ALTER TABLE chat_message
    ADD CONSTRAINT FK_CHAT_MESSAGE_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE checklist
    ADD CONSTRAINT FK_CHECKLIST_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_MAIN_CATEGORY FOREIGN KEY (main_category_id) REFERENCES category (id);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_SUB_CATEGORY FOREIGN KEY (sub_category_id) REFERENCES category (id);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_WEDDING FOREIGN KEY (wedding_id) REFERENCES wedding (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CHAT_ROOM FOREIGN KEY (chat_room_id) REFERENCES chat_room (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_category
    ADD CONSTRAINT FK_USER_CATEGORY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE user_category
    ADD CONSTRAINT FK_USER_CATEGORY_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USER_ROLE_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USER_ROLE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE wedding
    ADD CONSTRAINT FK_WEDDING_ON_BRIDE FOREIGN KEY (bride_id) REFERENCES users (id);

ALTER TABLE wedding
    ADD CONSTRAINT FK_WEDDING_ON_GROOM FOREIGN KEY (groom_id) REFERENCES users (id);

ALTER TABLE wishlist
    ADD CONSTRAINT FK_WISHLIST_ON_WEDDING FOREIGN KEY (wedding_id) REFERENCES wedding (id);

ALTER TABLE wish_item
    ADD CONSTRAINT FK_WISH_ITEM_ON_WISHLIST FOREIGN KEY (wishlist_id) REFERENCES wishlist (id);