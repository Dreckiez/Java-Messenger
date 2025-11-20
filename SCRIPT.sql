-- ======================
-- Core tables
-- ======================

-- user_info
CREATE TABLE user_info (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    hash_password VARCHAR(100) NOT NULL,
    role SMALLINT NOT NULL,
    gender SMALLINT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    address VARCHAR(300),
    birthday DATE,
    avatar_url TEXT,
    is_active BOOLEAN ,
    is_online BOOLEAN,
    is_accepted BOOLEAN,
    joined_at TIMESTAMP,
    updated_at TIMESTAMP,
    friend_count SMALLINT
);

-- verify_token
CREATE TABLE verify_token (
    verification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES user_info(user_id) ON DELETE CASCADE,
    token VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expired_at TIMESTAMP NOT NULL
);

-- verify_email_change_token
CREATE TABLE verify_email_change_token (
    verification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES user_info(user_id) ON DELETE CASCADE,
    token VARCHAR(50) NOT NULL,
    new_email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expired_at TIMESTAMP NOT NULL
);

-- request_password_reset
CREATE TABLE request_password_reset (
    request_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    token VARCHAR(50) NOT NULL,
    requested_at TIMESTAMP NOT NULL
);

-- device
CREATE TABLE device (
    device_id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    joined_at TIMESTAMP
);

-- record_online_user
CREATE TABLE record_online_user (
    session_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    online_at TIMESTAMP NOT NULL,
    offline_at TIMESTAMP
);

-- record_signin
CREATE TABLE record_signin (
    record_signin_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES user_info(user_id) ON DELETE SET NULL,
    signed_in_at TIMESTAMP NOT NULL,
    is_successful BOOLEAN
);

-- report
CREATE TABLE report (
    reporter_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    reported_user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    title VARCHAR(50),
    content TEXT,
    reported_at TIMESTAMP NOT NULL,
    PRIMARY KEY(reporter_id, reported_user_id, reported_at)
);

-- friend_request
CREATE TABLE friend_request (
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    receiver_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    status SMALLINT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    is_active BOOLEAN,
    PRIMARY KEY (sender_id, receiver_id, sent_at),
    CONSTRAINT uq_friend_request_sender_receiver UNIQUE (sender_id, receiver_id, sent_at),
    CONSTRAINT chk_friend_request_not_self CHECK (sender_id <> receiver_id)
);

-- friend
CREATE TABLE friend (
    user_id1 BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    user_id2 BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    made_friend_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id1, user_id2),
    CONSTRAINT chk_friend_order CHECK (user_id1 < user_id2)
);

-- user_block
CREATE TABLE block (
    blocker_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    blocked_user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    blocked_at TIMESTAMP NOT NULL,
    is_active BOOLEAN,
    removed_at TIMESTAMP,
    PRIMARY KEY (blocker_id, blocked_user_id, blocked_at),
    CONSTRAINT chk_block_not_self CHECK (blocker_id <> blocked_user_id)
);

-- private_conversation
CREATE TABLE private_conversation (
    private_conversation_id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    preview_message_id BIGINT,
    CONSTRAINT fk_private_user1 FOREIGN KEY (user1_id)
        REFERENCES user_info(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_private_user2 FOREIGN KEY (user2_id)
        REFERENCES user_info(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_private_order CHECK (user1_id < user2_id),
    CONSTRAINT uq_private_pair UNIQUE (user1_id, user2_id)
);

CREATE TABLE delete_private_conversation (
    private_conversation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    deleted_at TIMESTAMP,
    PRIMARY KEY (private_conversation_id, user_id),
    CONSTRAINT fk_pcu_conversation FOREIGN KEY (private_conversation_id)
        REFERENCES private_conversation(private_conversation_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_pcu_user FOREIGN KEY (user_id)
        REFERENCES user_info(user_id)
        ON DELETE CASCADE
);


-- private_conversation_message
CREATE TABLE private_conversation_message (
    private_conversation_message_id BIGSERIAL PRIMARY KEY,
    private_conversation_id BIGINT NOT NULL REFERENCES private_conversation(private_conversation_id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    type SMALLINT NOT NULL
);

ALTER TABLE private_conversation
ADD CONSTRAINT fk_private_preview
FOREIGN KEY (preview_message_id)
REFERENCES private_conversation_message(private_conversation_message_id)
DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE read_private_conversation_message (
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    private_conversation_id BIGINT NOT NULL REFERENCES private_conversation(private_conversation_id) ON DELETE CASCADE,
    private_conversation_message_id BIGINT NOT NULL REFERENCES private_conversation_message(private_conversation_message_id) ON DELETE SET NULL,
    read_at TIMESTAMP,
    PRIMARY KEY (user_id, private_conversation_id)
);

-- delete_private_conversation_message
CREATE TABLE delete_private_conversation_message (
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    private_conversation_message_id BIGINT NOT NULL REFERENCES private_conversation_message(private_conversation_message_id) ON DELETE CASCADE,
    deleted_at TIMESTAMP NOT NULL,
    is_all BOOLEAN,
    PRIMARY KEY (user_id, private_conversation_message_id)
);

-- group_conversation
CREATE TABLE group_conversation (
    group_conversation_id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    owner_id BIGINT REFERENCES user_info(user_id) ON DELETE SET NULL,
    preview_message_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    is_encrypted BOOLEAN
);

CREATE TABLE delete_group_conversation (
    group_conversation_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    deleted_at TIMESTAMP,
    PRIMARY KEY (group_conversation_id, member_id),
    CONSTRAINT fk_gcm_conversation FOREIGN KEY (group_conversation_id)
        REFERENCES group_conversation(group_conversation_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_gcm_member FOREIGN KEY (member_id)
        REFERENCES user_info(user_id)
        ON DELETE CASCADE
);

-- group_conversation_member
CREATE TABLE group_conversation_member (
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    joined_at TIMESTAMP NOT NULL,
    appointed_at TIMESTAMP,
    group_role SMALLINT,
    PRIMARY KEY (group_conversation_id, member_id)
);

-- group_conversation_message
CREATE TABLE group_conversation_message (
    group_conversation_message_id BIGSERIAL PRIMARY KEY,
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE SET NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    type SMALLINT NOT NULL
);

ALTER TABLE group_conversation
ADD CONSTRAINT fk_group_preview
FOREIGN KEY (preview_message_id)
REFERENCES group_conversation_message(group_conversation_message_id)
DEFERRABLE INITIALLY DEFERRED;

-- delete_group_conversation_message
CREATE TABLE delete_group_conversation_message (
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    group_conversation_message_id BIGINT NOT NULL REFERENCES group_conversation_message(group_conversation_message_id) ON DELETE CASCADE,
    deleted_at TIMESTAMP NOT NULL,
    is_all BOOLEAN,
    PRIMARY KEY (member_id, group_conversation_message_id)
);

-- group_conversation_read
CREATE TABLE read_group_conversation_message (
    group_conversation_message_id BIGINT NOT NULL REFERENCES group_conversation_message(group_conversation_message_id) ON DELETE CASCADE,
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    read_at TIMESTAMP NOT NULL,
    PRIMARY KEY (group_conversation_id, member_id)
);

-- encryption_group
CREATE TABLE encryption_group (
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    device_id UUID REFERENCES device(device_id) ON DELETE SET NULL,
    hash_secret_key VARCHAR(255),
    pin_code VARCHAR(20),
    PRIMARY KEY (user_id, group_conversation_id, device_id)
);

-- ======================
-- Indexes
-- ======================
CREATE INDEX idx_userinfo_email ON user_info(email);
CREATE INDEX idx_private_conv_user1 ON private_conversation(user1_id);
CREATE INDEX idx_private_conv_user2 ON private_conversation(user2_id);
CREATE INDEX idx_pcm_sender ON private_conversation_message(sender_id);
CREATE INDEX idx_gcm_group ON group_conversation_message(group_conversation_id);
CREATE INDEX idx_gcm_sender ON group_conversation_message(sender_id);
CREATE INDEX idx_friend_request_receiver ON friend_request(receiver_id);
CREATE INDEX idx_friend_request_sender ON friend_request(sender_id);
CREATE UNIQUE INDEX idx_delete_private_conversation_user
ON delete_private_conversation (user_id, private_conversation_id);
CREATE INDEX idx_delete_msg_user_deletedat
ON delete_private_conversation_message (user_id, deleted_at DESC);

-- ======================
-- Triggers
-- ======================
CREATE OR REPLACE FUNCTION trigger_set_timestamptz_updated_at()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = now();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_userinfo_updated_at
BEFORE UPDATE ON user_info
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamptz_updated_at();

CREATE TRIGGER trg_friend_request_updated_at
BEFORE UPDATE ON friend_request
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamptz_updated_at();

CREATE TRIGGER trg_private_msg_updated_at
BEFORE UPDATE ON private_conversation_message
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamptz_updated_at();

CREATE TRIGGER trg_group_msg_updated_at
BEFORE UPDATE ON group_conversation_message
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamptz_updated_at();