-- ======================
-- ENUM types
-- ======================
CREATE TYPE role_enum AS ENUM ('USER', 'ADMIN', 'MODERATOR');

CREATE TYPE friend_status_enum AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED');

CREATE TYPE gender_enum AS ENUM ('FEMALE', 'MALE', 'HIDDEN');

CREATE TYPE message_type_enum AS ENUM ('TEXT', 'IMAGE');

CREATE TYPE conversation_type_enum AS ENUM ('PRIVATE', 'GROUP');

-- ======================
-- Core tables
-- ======================

-- user_info: main user table
CREATE TABLE user_info (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    hash_password VARCHAR(100) NOT NULL,
    role role_enum NOT NULL DEFAULT 'USER',
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    city VARCHAR(50),
    district VARCHAR(50),
    street VARCHAR(50),
    country VARCHAR(50),
    birthday DATE,
    avatar_url VARCHAR(255),
    is_active BOOLEAN DEFAULT FALSE,
    is_online BOOLEAN DEFAULT FALSE,
    is_accepted BOOLEAN DEFAULT TRUE,
    joined_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

-- verify_token: one token per user (unique user_id)
CREATE TABLE verify_token (
    verification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES user_info(user_id) ON DELETE CASCADE,
    token VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    expired_at TIMESTAMP NOT NULL
);

-- request_password_reset
CREATE TABLE request_password_reset (
    request_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    token VARCHAR(50) NOT NULL,
    requested_at TIMESTAMP DEFAULT now()
);

-- device: devices user logged in from
CREATE TABLE device (
    device_id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT now()
);

-- record_online_user: record when user goes online/offline (audit)
CREATE TABLE record_online_user (
    session_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    online_at TIMESTAMP DEFAULT now(),
    offline_at TIMESTAMP
);

-- record_logging: general log table
CREATE TABLE record_logging (
    record_logging_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES user_info(user_id) ON DELETE SET NULL,
    logged_at TIMESTAMP DEFAULT now(),
    is_successful BOOLEAN DEFAULT TRUE
);

-- report: report one user by another (moderation)
CREATE TABLE report (
    report_id BIGSERIAL PRIMARY KEY,
    reporter_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    reported_user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    title VARCHAR(30),
    content TEXT,
    created_at TIMESTAMP DEFAULT now()
);



-- friend_request
CREATE TABLE friend_request (
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    receiver_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    status friend_status_enum NOT NULL DEFAULT 'PENDING',
    sent_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (sender_id, receiver_id, sent_at),
    CONSTRAINT uq_friend_request_sender_receiver UNIQUE (sender_id, receiver_id),
    CONSTRAINT chk_friend_request_not_self CHECK (sender_id <> receiver_id)
);

-- friend: accepted friendships (undirected). We'll store with user_a < user_b to avoid duplicates.
CREATE TABLE friend (
    user_1 BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    user_2 BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    made_friend_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_1, user_2),
    CONSTRAINT chk_friend_order CHECK (user_1 < user_2)
);

-- user_block: when a user blocks another
CREATE TABLE user_block (
    blocker_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    blocked_user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    blocked_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (blocker_id, blocked_user_id),
    CONSTRAINT chk_block_not_self CHECK (blocker_id <> blocked_user_id)
);

-- ======================
-- Private conversations & messages
-- ======================

-- private_conversation: between two users. Enforce user1 < user2 to ensure uniqueness independent of order.
CREATE TABLE private_conversation (
    private_conversation_id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    preview_message_id BIGINT,
    
    CONSTRAINT fk_private_user1 FOREIGN KEY (user1_id)
        REFERENCES user_info(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_private_user2 FOREIGN KEY (user2_id)
        REFERENCES user_info(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_private_order CHECK (user1_id < user2_id),
    CONSTRAINT uq_private_pair UNIQUE (user1_id, user2_id)
);

-- private_conversation_message
CREATE TABLE private_conversation_message (
    private_conversation_message_id BIGSERIAL PRIMARY KEY,
    private_conversation_id BIGINT NOT NULL REFERENCES private_conversation(private_conversation_id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP,
    type message_type_enum NOT NULL DEFAULT 'TEXT',
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP
);

ALTER TABLE private_conversation
ADD CONSTRAINT fk_private_preview
FOREIGN KEY (preview_message_id)
REFERENCES private_conversation_message(private_conversation_message_id)
DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE delete_private_conversation_message (
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE, 
    private_conversation_message_id BIGINT NOT NULL REFERENCES private_conversation_message(private_conversation_message_id) ON DELETE CASCADE,
    deleted_at TIMESTAMP DEFAULT now(),
    is_all BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, private_conversation_message_id)
);

CREATE TABLE group_conversation (
    group_conversation_id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    owner_id BIGINT REFERENCES user_info(user_id) ON DELETE SET NULL,
    preview_message_id BIGINT,
	  created_at TIMESTAMP DEFAULT now(),
    is_encrypted BOOLEAN DEFAULT FALSE
);

-- group_conversation_member
CREATE TABLE group_conversation_member (
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (group_conversation_id, member_id)
);

-- group_conversation_admin: subset of members with admin privileges
CREATE TABLE group_conversation_admin (
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    admin_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    appointed_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (group_conversation_id, admin_id)
);

-- group_conversation_message
CREATE TABLE group_conversation_message (
    group_conversation_message_id BIGSERIAL PRIMARY KEY,
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    sender_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE SET NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP,
    type message_type_enum NOT NULL DEFAULT 'TEXT'
);

ALTER TABLE group_conversation
ADD CONSTRAINT fk_group_preview
FOREIGN KEY (preview_message_id)
REFERENCES group_conversation_message(group_conversation_message_id)
DEFERRABLE INITIALLY DEFERRED;

-- delete_group_conversation_message (audit)
CREATE TABLE delete_group_conversation_message (
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    group_conversation_message_id BIGINT NOT NULL REFERENCES group_conversation_message(group_conversation_message_id) ON DELETE CASCADE,
    deleted_at TIMESTAMP DEFAULT now(),
    is_all BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (member_id, group_conversation_message_id)
);

-- group_conversation_read: track read receipts for group messages
CREATE TABLE group_conversation_read (
    group_conversation_message_id BIGINT NOT NULL REFERENCES group_conversation_message(group_conversation_message_id) ON DELETE CASCADE,
    member_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    read_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (group_conversation_message_id, member_id)
);

-- ======================
-- Encryption groups (for encrypted group chats)
-- ======================
CREATE TABLE encryption_group (
    user_id BIGINT NOT NULL REFERENCES user_info(user_id) ON DELETE CASCADE,
    group_conversation_id BIGINT NOT NULL REFERENCES group_conversation(group_conversation_id) ON DELETE CASCADE,
    device_id UUID REFERENCES device(device_id) ON DELETE SET NULL,
    hash_secret_key VARCHAR(255),
    pin_code VARCHAR(20),
    PRIMARY KEY (user_id, group_conversation_id, device_id)
);

-- ======================
-- Misc utilities / audit
-- ======================

-- record_password_reset (if needed separate from request_password_reset)
-- Already have request_password_reset above.

-- ======================
-- Indexes (helpful ones)
-- ======================
CREATE INDEX idx_userinfo_email ON user_info(email);
CREATE INDEX idx_private_conv_user1 ON private_conversation(user1_id);
CREATE INDEX idx_private_conv_user2 ON private_conversation(user2_id);
CREATE INDEX idx_pcm_sender ON private_conversation_message(sender_id);
CREATE INDEX idx_gcm_group ON group_conversation_message(group_conversation_id);
CREATE INDEX idx_gcm_sender ON group_conversation_message(sender_id);
CREATE INDEX idx_friend_request_receiver ON friend_request(receiver_id);
CREATE INDEX idx_friend_request_sender ON friend_request(sender_id);

-- ======================
-- Triggers (optional): update updated_at timestamp automatically
-- ======================
-- Helper function
CREATE OR REPLACE FUNCTION trigger_set_timestamptz_updated_at()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = now();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Attach trigger to tables that have updated_at
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
