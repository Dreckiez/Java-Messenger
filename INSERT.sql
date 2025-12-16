--
-- PostgreSQL database dump
--


-- Dumped from database version 16.10
-- Dumped by pg_dump version 16.10

-- Started on 2025-12-14 21:15:08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5094 (class 0 OID 48036)
-- Dependencies: 218
-- Data for Name: user_info; Type: TABLE DATA; Schema: public; Owner: -
--

SET SESSION AUTHORIZATION DEFAULT;

ALTER TABLE public.user_info DISABLE TRIGGER ALL;

INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (1, 'admin123', 'admin123@gmail.com', '$2a$10$vWiSkrRXXGhN3vb3aomXLe4xAG51UilgPrDH3y0yb67lTqVka3r4y', 1, 0, 'User', 'Super', '', NULL, '', true, false, true, '2025-12-12 22:57:54.037183', NULL, 0);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (4, 'charles123', 'charles123@fake.com', '$2a$10$XEj9gy65MOrbHi6H9CsNDeZjITuoQudps1zGCOXG/f2m3nbH3APDy', 0, 0, 'Charles', 'Catwright', '', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765619471/avatars/charles123.jpg', true, false, true, '2025-12-12 23:00:40.903627', '2025-12-14 10:36:25.622835', 0);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (3, 'bob123', 'bob123@fake.com', '$2a$10$WsnuMB3Ef3Qz1gMyQFSSBONsD8wRApFdpRB4WE.1.0W1ecn3T0cSW', 0, 0, 'Bob', 'Bob', '', NULL, NULL, true, false, true, '2025-12-12 23:00:20.714835', '2025-12-14 12:47:52.770783', 0);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (2, 'alice123', 'alice123@fake.com', '$2a$10$yh6qoT7s236rrpFhRLvo7uaX6JLsCumKmXHljKuGMSHp9DbKjFZkK', 0, 0, 'Alice', 'In Borderland', '', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765685239/avatars/alice124.jpg', true, false, true, '2025-12-12 23:00:05.694802', '2025-12-14 12:49:59.823939', 1);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (6, 'erik123', 'erik123@fake.com', '$2a$10$blisnUV4gYb1C9tXf84rceJp1w8u5AcKQcWe7AFdWaKoMdEZu8.oK', 0, 0, 'Erik', 'Kire', '', NULL, '', true, false, true, '2025-12-12 23:01:11.820903', '2025-12-14 12:50:03.964879', 2);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (5, 'dom123', 'dom123@fake.com', '$2a$10$HVVWfK5omrGIAV4K5ujm0.VTQgxzz58ILdROFSfe8Hms0r79dCnta', 0, 0, 'Dom', 'Cobb', 'dá', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765594951/avatars/dom123.jpg', true, false, true, '2025-12-12 23:00:54.459268', '2025-12-14 12:50:03.964879', 1);


ALTER TABLE public.user_info ENABLE TRIGGER ALL;

--
-- TOC entry 5108 (class 0 OID 48168)
-- Dependencies: 232
-- Data for Name: block; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.block DISABLE TRIGGER ALL;

INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 5, '2025-12-13 11:23:52.955482', false, '2025-12-13 11:59:00.235679');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 6, '2025-12-13 11:59:36.874912', false, '2025-12-13 11:59:44.157401');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 5, '2025-12-14 10:13:04.55587', false, '2025-12-14 10:13:24.010582');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 11:14:58.578342', false, '2025-12-14 11:18:03.519032');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 11:19:47.328983', false, '2025-12-14 11:20:36.134326');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 12:47:52.782418', false, '2025-12-14 12:48:50.695181');


ALTER TABLE public.block ENABLE TRIGGER ALL;

--
-- TOC entry 5117 (class 0 OID 48274)
-- Dependencies: 241
-- Data for Name: group_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.group_conversation DISABLE TRIGGER ALL;

INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (2, 'Tiểu đội 3', NULL, 2, NULL, '2025-12-12 23:12:03.424876', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (4, 'anh em 31', NULL, 2, NULL, '2025-12-12 23:12:16.197772', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (1, 'Tiểu đội 1', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765619040/group_avatars/1.jpg', 2, NULL, '2025-12-12 23:11:57.45923', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (3, 'Tiểu đội 2', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765633869/group_avatars/3.jpg', 2, NULL, '2025-12-12 23:12:09.479481', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (6, 'Hội quán', NULL, 2, NULL, '2025-12-13 23:09:40.612005', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (7, 'Group bike bike', NULL, 2, NULL, '2025-12-14 09:35:19.384333', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (8, 'Group 1', NULL, 2, NULL, '2025-12-14 09:41:19.965542', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (9, 'Đánh bắt thủy hải sản', NULL, 2, NULL, '2025-12-14 09:44:26.974821', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (13, 'GG', NULL, 2, NULL, '2025-12-14 09:58:18.564173', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (15, 'Cho Tôi Lang Thang', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765685017/group_avatars/15.jpg', 2, NULL, '2025-12-14 10:43:00.193118', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (16, 'Em Dạo Này', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765685066/group_avatars/16.jpg', 2, NULL, '2025-12-14 10:43:14.717984', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (12, 'Xanh Màu Lá Xanh Đại Dương', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765685136/group_avatars/12.jpg', 2, NULL, '2025-12-14 09:55:48.523345', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (14, 'Chuyện Rằng', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765688980/group_avatars/14.jpg', 2, NULL, '2025-12-14 10:07:00.072849', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (19, 'group with dom', NULL, 6, NULL, '2025-12-14 12:50:44.059407', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (11, 'Khóc Đấy', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765700536/group_avatars/11.jpg', 2, NULL, '2025-12-14 09:54:50.411817', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (10, 'Bút Chì Bạc', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765700627/group_avatars/10.jpg', 2, NULL, '2025-12-14 09:50:07.589466', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (17, 'Tranh luận Group', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765691318/group_avatars/17.png', 2, NULL, '2025-12-14 11:14:52.433514', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (5, 'Tiểu đội 3', NULL, 2, 2, '2025-12-12 23:12:22.661743', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (18, 'haha 123', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765721018/group_avatars/18.jpg', 6, NULL, '2025-12-14 12:50:29.010802', false);


ALTER TABLE public.group_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5118 (class 0 OID 48287)
-- Dependencies: 242
-- Data for Name: delete_group_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_group_conversation DISABLE TRIGGER ALL;

INSERT INTO public.delete_group_conversation (group_conversation_id, member_id, deleted_at) VALUES (5, 2, '2025-12-13 12:54:20.256313');
INSERT INTO public.delete_group_conversation (group_conversation_id, member_id, deleted_at) VALUES (4, 2, '2025-12-13 22:38:24.719251');
INSERT INTO public.delete_group_conversation (group_conversation_id, member_id, deleted_at) VALUES (2, 2, '2025-12-13 22:51:02.99453');


ALTER TABLE public.delete_group_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5121 (class 0 OID 48318)
-- Dependencies: 245
-- Data for Name: group_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.group_conversation_message DISABLE TRIGGER ALL;

INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (1, 5, 2, 'Hello', '2025-12-14 20:19:40.91094', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (2, 5, 4, 'Hello', '2025-12-14 20:23:18.714591', NULL, 0);


ALTER TABLE public.group_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5122 (class 0 OID 48341)
-- Dependencies: 246
-- Data for Name: delete_group_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_group_conversation_message DISABLE TRIGGER ALL;



ALTER TABLE public.delete_group_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5110 (class 0 OID 48185)
-- Dependencies: 234
-- Data for Name: private_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.private_conversation DISABLE TRIGGER ALL;

INSERT INTO public.private_conversation (private_conversation_id, user1_id, user2_id, created_at, preview_message_id) VALUES (15, 2, 6, '2025-12-14 12:49:59.837677', NULL);
INSERT INTO public.private_conversation (private_conversation_id, user1_id, user2_id, created_at, preview_message_id) VALUES (16, 5, 6, '2025-12-14 12:50:03.977732', NULL);


ALTER TABLE public.private_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5111 (class 0 OID 48204)
-- Dependencies: 235
-- Data for Name: delete_private_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_private_conversation DISABLE TRIGGER ALL;



ALTER TABLE public.delete_private_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5113 (class 0 OID 48220)
-- Dependencies: 237
-- Data for Name: private_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.private_conversation_message DISABLE TRIGGER ALL;

INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (4, 15, 2, 'Hi, this is user 2 in conversation 15', '2025-12-14 16:26:33.89448', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (5, 15, 6, 'Hello user 2, this is user 6', '2025-12-14 16:26:33.89448', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (6, 16, 5, 'Hi, this is user 5 in conversation 16', '2025-12-14 16:26:43.854', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (7, 16, 6, 'Hello user 5, this is user 6', '2025-12-14 16:26:43.854', NULL, 0);


ALTER TABLE public.private_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5115 (class 0 OID 48258)
-- Dependencies: 239
-- Data for Name: delete_private_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_private_conversation_message DISABLE TRIGGER ALL;



ALTER TABLE public.delete_private_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5101 (class 0 OID 48088)
-- Dependencies: 225
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.device DISABLE TRIGGER ALL;



ALTER TABLE public.device ENABLE TRIGGER ALL;

--
-- TOC entry 5124 (class 0 OID 48376)
-- Dependencies: 248
-- Data for Name: encryption_group; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.encryption_group DISABLE TRIGGER ALL;



ALTER TABLE public.encryption_group ENABLE TRIGGER ALL;

--
-- TOC entry 5107 (class 0 OID 48152)
-- Dependencies: 231
-- Data for Name: friend; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.friend DISABLE TRIGGER ALL;

INSERT INTO public.friend (user_id1, user_id2, made_friend_at) VALUES (2, 6, '2025-12-14 12:49:59.82216');
INSERT INTO public.friend (user_id1, user_id2, made_friend_at) VALUES (5, 6, '2025-12-14 12:50:03.96368');


ALTER TABLE public.friend ENABLE TRIGGER ALL;

--
-- TOC entry 5106 (class 0 OID 48136)
-- Dependencies: 230
-- Data for Name: friend_request; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.friend_request DISABLE TRIGGER ALL;

INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 1, '2025-12-12 23:03:47.507642', '2025-12-12 23:06:02.957937', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:46.451204', '2025-12-13 10:59:46.581025', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 4, 1, '2025-12-12 23:03:50.891432', '2025-12-12 23:06:59.796653', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 1, '2025-12-12 23:03:52.555459', '2025-12-12 23:08:03.388976', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:46.692145', '2025-12-13 10:59:46.82868', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 1, '2025-12-12 23:03:54.23934', '2025-12-12 23:10:10.862786', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 07:58:11.193613', '2025-12-13 09:38:36.813911', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 07:04:33.136916', '2025-12-13 09:39:51.182831', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:38:38.182917', '2025-12-13 09:57:49.335864', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:50.126636', '2025-12-13 09:57:50.599606', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:51.216508', '2025-12-13 09:57:51.633002', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:51.77675', '2025-12-13 09:57:51.872811', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:51.994403', '2025-12-13 09:57:52.12319', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:52.334855', '2025-12-13 09:57:52.450235', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:52.558648', '2025-12-13 09:57:52.689367', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:53.009496', '2025-12-13 09:57:53.122362', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:53.248738', '2025-12-13 09:57:53.385728', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:53.469037', '2025-12-13 09:57:53.603547', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:53.735028', '2025-12-13 09:57:53.847664', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:53.973287', '2025-12-13 09:57:54.127535', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:54.230953', '2025-12-13 09:57:54.484687', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:54.607906', '2025-12-13 09:57:54.767527', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:54.872117', '2025-12-13 09:57:55.008289', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:55.376913', '2025-12-13 09:57:55.517798', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:55.759301', '2025-12-13 09:57:55.968951', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:56.116991', '2025-12-13 09:57:56.286315', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:56.457007', '2025-12-13 09:57:56.62356', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:56.783411', '2025-12-13 09:57:56.95195', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-13 09:57:57.503779', '2025-12-13 09:57:57.937147', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (4, 2, 1, '2025-12-13 07:48:36.15541', '2025-12-13 09:58:05.394486', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 09:58:08.610128', '2025-12-13 09:58:09.432791', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 09:58:09.646468', '2025-12-13 09:58:09.798679', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 09:58:09.924051', '2025-12-13 09:58:10.039932', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (5, 4, 0, '2025-12-13 10:00:25.758583', NULL, true);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:46.963781', '2025-12-13 10:59:47.101808', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 2, '2025-12-13 09:57:58.295653', '2025-12-13 10:00:54.376496', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (5, 2, 1, '2025-12-13 10:00:54.381664', '2025-12-13 10:39:40.567613', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:47.203536', '2025-12-13 10:59:47.341355', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (6, 2, 2, '2025-12-13 10:39:06.858163', '2025-12-13 10:49:26.695142', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 10:49:26.699731', '2025-12-13 10:49:27.797636', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 10:49:28.425946', '2025-12-13 10:49:29.027548', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 10:49:29.989239', '2025-12-13 10:49:30.782301', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 09:39:52.719789', '2025-12-13 10:59:07.285879', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:17.086871', '2025-12-13 10:59:42.789686', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:43.511395', '2025-12-13 10:59:44.066038', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:44.456995', '2025-12-13 10:59:44.733457', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:44.89823', '2025-12-13 10:59:45.045345', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:45.292342', '2025-12-13 10:59:45.436861', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:45.557053', '2025-12-13 10:59:45.700638', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:45.821594', '2025-12-13 10:59:45.966024', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:46.206636', '2025-12-13 10:59:46.325442', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:47.47009', '2025-12-13 10:59:52.500982', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 10:59:52.627606', '2025-12-13 10:59:53.020344', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:21.637724', '2025-12-13 11:01:22.141638', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:22.515583', '2025-12-13 11:01:22.691811', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:22.904224', '2025-12-13 11:01:23.244734', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:23.611665', '2025-12-13 11:01:23.861276', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:24.148647', '2025-12-13 11:01:24.436179', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:24.827202', '2025-12-13 11:01:25.0493', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:25.468511', '2025-12-13 11:01:25.804486', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:52.802932', '2025-12-13 11:01:54.372127', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:01:55.1421', '2025-12-13 11:05:02.148892', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:03.035207', '2025-12-13 11:05:03.779953', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:04.292089', '2025-12-13 11:05:04.92471', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:05.372664', '2025-12-13 11:05:05.875402', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:06.211721', '2025-12-13 11:05:06.731491', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:07.234851', '2025-12-13 11:05:07.643122', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:08.059389', '2025-12-13 11:05:08.762796', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:05:09.364395', '2025-12-13 11:05:10.392884', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:40.588046', '2025-12-13 11:07:41.077353', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:41.530893', '2025-12-13 11:07:42.131632', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:42.812613', '2025-12-13 11:07:43.275742', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:43.556756', '2025-12-13 11:07:44.003982', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:44.197452', '2025-12-13 11:07:44.340466', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:44.716166', '2025-12-13 11:07:44.861343', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:44.989398', '2025-12-13 11:07:45.106157', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:45.548062', '2025-12-13 11:07:45.961341', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:46.10877', '2025-12-13 11:07:46.40415', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:46.595686', '2025-12-13 11:07:46.761315', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:07:46.908255', '2025-12-13 11:07:47.171714', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (6, 2, 1, '2025-12-13 11:02:45.557544', '2025-12-13 11:59:10.982497', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (5, 2, 1, '2025-12-13 21:28:10.627805', '2025-12-13 21:28:27.853938', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:35:53.962361', '2025-12-13 21:35:54.83426', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:35:55.280331', '2025-12-13 21:35:55.760699', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:35:56.047345', '2025-12-13 21:35:56.414818', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:35:56.618233', '2025-12-13 21:35:56.837833', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:35:58.529042', '2025-12-13 21:35:59.920828', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:36:00.785289', '2025-12-13 21:36:01.592807', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:36:02.865777', '2025-12-13 21:36:03.78394', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:36:04.430887', '2025-12-13 21:36:04.694737', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:36:57.578553', '2025-12-13 21:36:58.554705', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:36:59.234537', '2025-12-13 21:36:59.742819', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:46.647362', '2025-12-13 21:40:47.378225', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:47.801125', '2025-12-13 21:40:47.969089', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:48.11809', '2025-12-13 21:40:48.329313', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:48.436096', '2025-12-13 21:40:48.535553', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:48.654108', '2025-12-13 21:40:48.766451', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:48.878409', '2025-12-13 21:40:48.992289', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:49.118502', '2025-12-13 21:40:49.230958', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 3, '2025-12-13 11:08:03.621249', '2025-12-13 21:54:48.548401', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (5, 6, 1, '2025-12-13 10:00:26.590986', '2025-12-14 12:50:03.980979', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:49.343078', '2025-12-13 21:40:49.500725', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:49.608279', '2025-12-13 21:40:49.703654', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:49.837946', '2025-12-13 21:40:49.95948', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:50.163255', '2025-12-13 21:40:50.462824', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:50.77605', '2025-12-13 21:40:50.927554', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:51.216782', '2025-12-13 21:40:51.407744', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:51.824889', '2025-12-13 21:40:52.206794', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:52.592174', '2025-12-13 21:40:52.735581', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:52.893229', '2025-12-13 21:40:53.069769', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:53.540886', '2025-12-13 21:40:53.697437', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:53.937261', '2025-12-13 21:40:54.057383', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:54.190925', '2025-12-13 21:40:54.415537', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:54.533367', '2025-12-13 21:40:54.661671', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:54.775296', '2025-12-13 21:40:55.014864', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:55.143126', '2025-12-13 21:40:55.26134', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:55.392249', '2025-12-13 21:40:55.519771', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:55.621255', '2025-12-13 21:40:55.750487', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:55.863139', '2025-12-13 21:40:55.990812', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:56.102982', '2025-12-13 21:40:56.223646', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:56.487714', '2025-12-13 21:40:56.653184', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:56.77405', '2025-12-13 21:40:57.013662', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:57.181215', '2025-12-13 21:40:57.331872', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:40:57.484803', '2025-12-13 21:41:31.689598', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:32.426254', '2025-12-13 21:41:32.863809', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:33.589392', '2025-12-13 21:41:33.777475', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:33.952398', '2025-12-13 21:41:34.090341', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:34.334367', '2025-12-13 21:41:34.560202', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:35.10423', '2025-12-13 21:41:35.383222', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:35.889204', '2025-12-13 21:41:36.866694', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:37.192506', '2025-12-13 21:41:37.860772', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:38.093463', '2025-12-13 21:41:38.599904', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:38.86381', '2025-12-13 21:41:39.495028', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:39.717336', '2025-12-13 21:41:40.222701', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:41.527884', '2025-12-13 21:41:42.07315', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:41:42.344307', '2025-12-13 21:41:42.903428', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:49:27.299138', '2025-12-13 21:49:27.882484', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:49:28.264282', '2025-12-13 21:49:28.59201', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:49:29.075771', '2025-12-13 21:49:29.438586', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:49:53.64269', '2025-12-13 21:49:54.369033', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:49:54.767535', '2025-12-13 21:49:55.437745', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:17.816772', '2025-12-13 21:50:18.521591', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:18.8053', '2025-12-13 21:50:19.236048', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:39.456331', '2025-12-13 21:50:40.12978', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:40.568391', '2025-12-13 21:50:41.192193', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:41.596184', '2025-12-13 21:50:42.823553', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:43.204894', '2025-12-13 21:50:43.638393', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:44.000148', '2025-12-13 21:50:44.380139', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:44.77513', '2025-12-13 21:50:45.286146', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:45.712082', '2025-12-13 21:50:46.302616', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:46.548251', '2025-12-13 21:50:46.893512', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:47.182041', '2025-12-13 21:50:47.516955', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:50:47.797228', '2025-12-13 21:50:48.147862', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:38.415623', '2025-12-13 21:51:39.1373', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:39.46271', '2025-12-13 21:51:39.916724', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:40.181502', '2025-12-13 21:51:40.551438', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:40.948797', '2025-12-13 21:51:41.541207', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:41.925333', '2025-12-13 21:51:42.533891', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:42.839835', '2025-12-13 21:51:43.151068', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:43.454626', '2025-12-13 21:51:43.623978', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:43.877251', '2025-12-13 21:51:44.025021', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:44.823346', '2025-12-13 21:51:45.102121', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:51:45.380778', '2025-12-13 21:51:45.87007', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:52:24.232174', '2025-12-13 21:52:25.072393', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:52:25.52764', '2025-12-13 21:52:26.029539', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:52:26.342036', '2025-12-13 21:52:26.846527', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:52:27.117378', '2025-12-13 21:52:27.253146', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:52:27.926455', '2025-12-13 21:52:28.791109', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:33.288214', '2025-12-13 21:53:34.080302', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:34.582869', '2025-12-13 21:53:35.933588', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:36.470779', '2025-12-13 21:53:36.95206', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:37.368001', '2025-12-13 21:53:37.812647', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:38.29434', '2025-12-13 21:53:38.795978', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:40.343411', '2025-12-13 21:53:41.702886', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:42.158609', '2025-12-13 21:53:42.446228', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:42.772548', '2025-12-13 21:53:43.750159', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:53:44.168007', '2025-12-13 21:53:44.55126', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:08.19137', '2025-12-13 21:54:09.126017', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:10.134945', '2025-12-13 21:54:10.878613', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:11.342353', '2025-12-13 21:54:11.831579', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:12.036099', '2025-12-13 21:54:12.21465', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:12.503433', '2025-12-13 21:54:12.980326', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:13.165255', '2025-12-13 21:54:13.341425', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:54:13.526561', '2025-12-13 21:54:13.966767', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:04.488488', '2025-12-13 21:55:08.175731', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:08.671295', '2025-12-13 21:55:09.175326', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:09.382675', '2025-12-13 21:55:09.591465', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:09.759413', '2025-12-13 21:55:09.911639', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:10.059497', '2025-12-13 21:55:10.204444', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:10.341283', '2025-12-13 21:55:10.782302', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:10.942506', '2025-12-13 21:55:11.261162', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:11.719477', '2025-12-13 21:55:12.486229', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:12.688444', '2025-12-13 21:55:12.853645', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:13.022621', '2025-12-13 21:55:13.206712', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:13.397977', '2025-12-13 21:55:13.596969', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:13.999368', '2025-12-13 21:55:14.688445', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:14.861373', '2025-12-13 21:55:15.037479', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:15.22303', '2025-12-13 21:55:15.398091', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 3, '2025-12-13 21:55:15.748808', '2025-12-13 21:55:16.044684', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 4, 3, '2025-12-13 21:55:16.703211', '2025-12-13 21:55:16.919583', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 4, 3, '2025-12-13 21:55:17.358071', '2025-12-13 21:55:18.524935', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 1, '2025-12-13 21:55:19.50334', '2025-12-13 23:09:37.557111', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:55:41.597574', '2025-12-13 21:55:42.089709', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:55:42.38914', '2025-12-13 21:55:42.764991', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:55:42.888192', '2025-12-13 21:55:43.533054', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 3, '2025-12-13 21:55:43.948945', '2025-12-13 21:55:44.557018', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (3, 2, 1, '2025-12-14 10:38:29.485606', '2025-12-14 10:38:52.897146', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 1, '2025-12-14 11:18:07.439961', '2025-12-14 11:18:31.218991', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 1, '2025-12-14 10:13:30.075448', '2025-12-14 11:20:10.272401', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 1, '2025-12-14 11:20:46.62014', '2025-12-14 11:21:30.052431', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 1, '2025-12-14 11:20:47.099664', '2025-12-14 11:21:53.683171', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (6, 4, 0, '2025-12-14 12:49:12.662519', NULL, true);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 6, 1, '2025-12-14 12:49:41.732526', '2025-12-14 12:49:59.841453', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 3, 0, '2025-12-14 16:51:44.367999', NULL, true);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 0, '2025-12-14 16:51:44.780598', NULL, true);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 4, 0, '2025-12-14 16:51:51.195998', NULL, true);


ALTER TABLE public.friend_request ENABLE TRIGGER ALL;

--
-- TOC entry 5119 (class 0 OID 48302)
-- Dependencies: 243
-- Data for Name: group_conversation_member; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.group_conversation_member DISABLE TRIGGER ALL;

INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (1, 2, '2025-12-12 23:11:57.464653', '2025-12-12 23:11:57.464653', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (1, 3, '2025-12-12 23:11:57.471174', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (1, 5, '2025-12-12 23:11:57.47827', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (1, 6, '2025-12-12 23:11:57.482351', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (2, 5, '2025-12-12 23:12:03.436497', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (2, 6, '2025-12-12 23:12:03.440046', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (3, 2, '2025-12-12 23:12:09.487804', '2025-12-12 23:12:09.487804', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (3, 3, '2025-12-12 23:12:09.491343', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (3, 4, '2025-12-12 23:12:09.494347', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (4, 4, '2025-12-12 23:12:16.212987', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (4, 6, '2025-12-12 23:12:16.215528', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (5, 2, '2025-12-12 23:12:22.668718', '2025-12-12 23:12:22.668718', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (5, 4, '2025-12-12 23:12:22.674236', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (1, 4, '2025-12-12 23:11:57.475288', NULL, 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (5, 6, '2025-12-12 23:12:22.677235', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (2, 3, '2025-12-12 23:12:03.433408', NULL, 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (4, 3, '2025-12-12 23:12:16.208981', NULL, 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (6, 2, '2025-12-13 23:09:40.6304', '2025-12-13 23:09:40.6304', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (6, 3, '2025-12-13 23:09:40.646627', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (6, 5, '2025-12-14 00:17:48.632422', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (7, 2, '2025-12-14 09:35:19.387248', '2025-12-14 09:35:19.387248', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (7, 3, '2025-12-14 09:35:19.396587', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (7, 5, '2025-12-14 09:35:19.400595', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (8, 2, '2025-12-14 09:41:19.972607', '2025-12-14 09:41:19.972607', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (8, 4, '2025-12-14 09:41:19.975869', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (9, 2, '2025-12-14 09:44:26.981835', '2025-12-14 09:44:26.981835', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (9, 5, '2025-12-14 09:44:26.985346', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (10, 2, '2025-12-14 09:50:07.597768', '2025-12-14 09:50:07.597768', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (10, 3, '2025-12-14 09:50:07.601283', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (11, 2, '2025-12-14 09:54:50.418831', '2025-12-14 09:54:50.418831', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (11, 3, '2025-12-14 09:54:50.42194', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (12, 2, '2025-12-14 09:55:48.531678', '2025-12-14 09:55:48.531678', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (12, 3, '2025-12-14 09:55:48.534698', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (13, 2, '2025-12-14 09:58:18.568175', '2025-12-14 09:58:18.568175', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (13, 5, '2025-12-14 09:58:18.5737', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (14, 2, '2025-12-14 10:07:00.079849', '2025-12-14 10:07:00.079849', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (14, 3, '2025-12-14 10:07:00.084888', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (15, 2, '2025-12-14 10:43:00.198488', '2025-12-14 10:43:00.198488', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (15, 3, '2025-12-14 10:43:00.205528', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (16, 2, '2025-12-14 10:43:14.72648', '2025-12-14 10:43:14.72648', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (16, 3, '2025-12-14 10:43:14.730836', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (17, 2, '2025-12-14 11:14:52.435515', '2025-12-14 11:14:52.435515', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (17, 3, '2025-12-14 11:14:52.439518', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (18, 6, '2025-12-14 12:50:29.017825', '2025-12-14 12:50:29.017825', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (18, 2, '2025-12-14 12:50:29.020062', NULL, 0);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (19, 6, '2025-12-14 12:50:44.067408', '2025-12-14 12:50:44.067408', 1);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role) VALUES (19, 5, '2025-12-14 12:50:44.070917', NULL, 0);


ALTER TABLE public.group_conversation_member ENABLE TRIGGER ALL;

--
-- TOC entry 5123 (class 0 OID 48356)
-- Dependencies: 247
-- Data for Name: read_group_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.read_group_conversation_message DISABLE TRIGGER ALL;



ALTER TABLE public.read_group_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5114 (class 0 OID 48243)
-- Dependencies: 238
-- Data for Name: read_private_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.read_private_conversation_message DISABLE TRIGGER ALL;

INSERT INTO public.read_private_conversation_message (user_id, private_conversation_message_id, read_at) VALUES (2, 5, '2025-12-14 16:26:54.415155');


ALTER TABLE public.read_private_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5102 (class 0 OID 48098)
-- Dependencies: 226
-- Data for Name: record_online_user; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.record_online_user DISABLE TRIGGER ALL;



ALTER TABLE public.record_online_user ENABLE TRIGGER ALL;

--
-- TOC entry 5104 (class 0 OID 48109)
-- Dependencies: 228
-- Data for Name: record_signin; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.record_signin DISABLE TRIGGER ALL;

INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (1, 2, '2025-12-12 23:02:09.66141', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (2, 3, '2025-12-12 23:03:10.746475', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (3, 4, '2025-12-12 23:06:20.251555', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (4, 5, '2025-12-12 23:07:12.688773', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (5, 6, '2025-12-12 23:08:11.078621', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (6, 6, '2025-12-12 23:08:17.94777', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (7, 2, '2025-12-12 23:13:27.820211', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (8, 2, '2025-12-12 23:16:59.219139', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (9, 2, '2025-12-12 23:18:22.731172', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (10, 2, '2025-12-12 23:29:17.611376', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (11, 2, '2025-12-12 23:30:14.489748', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (12, 2, '2025-12-12 23:32:35.576722', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (13, 2, '2025-12-12 23:34:09.6839', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (14, 2, '2025-12-12 23:34:56.496997', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (15, 2, '2025-12-12 23:35:29.208531', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (16, 2, '2025-12-12 23:35:49.558672', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (17, 2, '2025-12-12 23:37:16.1293', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (18, 2, '2025-12-12 23:38:35.545373', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (19, 2, '2025-12-12 23:38:39.77551', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (20, 2, '2025-12-12 23:41:34.593647', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (21, 2, '2025-12-12 23:44:25.49708', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (22, 2, '2025-12-12 23:44:26.702085', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (23, 2, '2025-12-12 23:44:27.077889', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (24, 2, '2025-12-12 23:44:27.388354', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (25, 2, '2025-12-12 23:44:27.628833', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (26, 2, '2025-12-12 23:44:27.869044', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (27, 2, '2025-12-12 23:44:28.100092', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (28, 2, '2025-12-12 23:44:29.696941', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (29, 2, '2025-12-12 23:57:21.785168', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (30, 2, '2025-12-13 00:02:20.819703', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (31, 2, '2025-12-13 00:04:51.938636', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (32, 2, '2025-12-13 00:09:17.218757', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (33, 2, '2025-12-13 00:11:46.386201', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (34, 2, '2025-12-13 00:13:29.562525', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (35, 2, '2025-12-13 00:13:33.128256', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (36, 2, '2025-12-13 00:14:58.563116', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (37, 2, '2025-12-13 00:16:24.938473', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (38, 2, '2025-12-13 06:56:14.484807', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (39, 2, '2025-12-13 06:57:59.386739', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (40, 2, '2025-12-13 06:58:51.994177', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (41, 2, '2025-12-13 07:01:06.674236', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (42, 2, '2025-12-13 07:10:12.668075', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (43, 2, '2025-12-13 07:12:27.161945', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (44, 2, '2025-12-13 07:24:59.939515', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (45, 2, '2025-12-13 07:43:58.927129', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (46, 2, '2025-12-13 07:44:53.473293', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (47, 2, '2025-12-13 07:46:13.289732', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (48, 2, '2025-12-13 07:47:36.411819', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (49, 4, '2025-12-13 07:48:18.826425', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (50, 2, '2025-12-13 07:52:55.305743', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (51, 2, '2025-12-13 07:58:05.521758', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (52, 5, '2025-12-13 07:58:29.704057', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (53, 2, '2025-12-13 08:14:29.046301', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (54, 2, '2025-12-13 08:15:45.752229', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (55, 2, '2025-12-13 08:16:21.019443', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (56, 2, '2025-12-13 08:16:50.944517', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (57, 2, '2025-12-13 08:21:31.697805', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (58, 5, '2025-12-13 08:22:11.502948', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (59, 2, '2025-12-13 08:26:27.088629', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (60, 2, '2025-12-13 08:32:45.739111', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (61, 2, '2025-12-13 08:33:32.81668', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (62, 2, '2025-12-13 08:34:47.681866', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (63, 2, '2025-12-13 08:36:19.814623', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (64, 2, '2025-12-13 08:38:52.413477', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (65, 2, '2025-12-13 08:40:07.864246', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (66, 2, '2025-12-13 08:59:05.761522', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (67, 2, '2025-12-13 08:59:09.489574', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (68, 2, '2025-12-13 09:03:27.891814', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (69, 2, '2025-12-13 09:04:54.044546', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (70, 2, '2025-12-13 09:05:26.858367', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (71, 2, '2025-12-13 09:06:00.505253', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (72, 2, '2025-12-13 09:08:23.3852', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (73, 2, '2025-12-13 09:12:15.145195', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (74, 2, '2025-12-13 09:12:42.04308', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (75, 2, '2025-12-13 09:15:29.584657', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (76, 2, '2025-12-13 09:24:09.338656', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (77, 2, '2025-12-13 09:25:20.107244', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (78, 2, '2025-12-13 09:33:39.065451', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (79, 2, '2025-12-13 09:36:07.11519', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (80, 2, '2025-12-13 09:36:56.035875', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (81, 2, '2025-12-13 09:38:13.155755', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (82, 2, '2025-12-13 09:57:44.061615', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (83, 5, '2025-12-13 10:00:08.227922', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (84, 2, '2025-12-13 10:07:51.297012', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (85, 2, '2025-12-13 10:08:48.554305', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (86, 2, '2025-12-13 10:15:11.144233', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (87, 2, '2025-12-13 10:16:20.993914', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (88, 2, '2025-12-13 10:24:47.034112', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (89, 2, '2025-12-13 10:26:24.700152', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (90, 2, '2025-12-13 10:29:24.94083', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (91, 2, '2025-12-13 10:30:15.698588', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (92, 2, '2025-12-13 10:31:24.251043', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (93, 2, '2025-12-13 10:32:17.345869', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (94, 2, '2025-12-13 10:33:00.671655', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (95, 2, '2025-12-13 10:33:50.122973', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (96, 2, '2025-12-13 10:35:26.250275', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (97, 2, '2025-12-13 10:35:51.322189', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (98, 2, '2025-12-13 10:36:09.299823', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (99, 2, '2025-12-13 10:37:40.178528', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (100, 2, '2025-12-13 10:38:01.525492', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (101, 6, '2025-12-13 10:38:37.112159', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (102, 2, '2025-12-13 10:39:19.50392', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (103, 2, '2025-12-13 10:48:54.015821', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (104, 2, '2025-12-13 10:53:03.490643', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (105, 2, '2025-12-13 10:53:08.239434', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (106, 2, '2025-12-13 10:56:55.516587', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (107, 2, '2025-12-13 10:58:41.947415', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (108, 2, '2025-12-13 10:59:35.580665', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (109, 2, '2025-12-13 11:01:13.610011', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (110, 6, '2025-12-13 11:02:41.116628', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (111, 2, '2025-12-13 11:04:46.011392', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (112, 2, '2025-12-13 11:07:06.184746', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (113, 3, '2025-12-13 11:08:21.298384', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (114, 2, '2025-12-13 11:10:59.999421', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (115, 3, '2025-12-13 11:11:32.551828', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (116, 2, '2025-12-13 11:13:30.289965', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (117, 3, '2025-12-13 11:13:48.683979', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (118, 2, '2025-12-13 11:18:25.722346', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (119, 3, '2025-12-13 11:18:51.705782', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (120, 2, '2025-12-13 11:23:20.906207', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (121, 2, '2025-12-13 11:49:03.633604', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (122, 2, '2025-12-13 11:50:35.400057', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (123, 2, '2025-12-13 11:50:37.815774', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (124, 2, '2025-12-13 11:51:49.332132', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (125, 2, '2025-12-13 11:52:59.826602', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (126, 2, '2025-12-13 11:53:26.128138', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (127, 2, '2025-12-13 11:57:13.587323', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (128, 2, '2025-12-13 11:58:44.071896', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (129, 2, '2025-12-13 12:22:13.043332', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (130, 2, '2025-12-13 12:23:23.013469', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (131, 2, '2025-12-13 12:35:53.579322', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (132, 2, '2025-12-13 12:39:22.659765', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (133, 4, '2025-12-13 12:47:22.793269', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (134, 2, '2025-12-13 12:49:00.030887', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (135, 2, '2025-12-13 12:54:13.963965', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (136, 2, '2025-12-13 12:58:14.591571', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (137, 2, '2025-12-13 13:05:02.362715', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (138, 2, '2025-12-13 13:06:19.917748', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (140, 2, '2025-12-13 13:13:23.66925', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (139, 2, '2025-12-13 13:08:04.731219', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (141, 2, '2025-12-13 13:14:52.63422', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (142, 2, '2025-12-13 14:38:47.286684', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (143, 2, '2025-12-13 14:39:33.850428', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (144, 2, '2025-12-13 14:57:07.931128', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (145, 2, '2025-12-13 14:59:45.277144', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (146, 2, '2025-12-13 15:52:31.271842', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (147, 2, '2025-12-13 16:11:12.451257', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (148, 2, '2025-12-13 16:12:04.296623', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (149, 2, '2025-12-13 16:15:03.530029', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (150, 2, '2025-12-13 16:22:51.598393', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (151, 2, '2025-12-13 16:24:41.879276', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (152, 2, '2025-12-13 16:27:34.033769', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (153, 2, '2025-12-13 16:32:45.207756', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (154, 2, '2025-12-13 16:34:41.055152', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (155, 2, '2025-12-13 16:39:29.112144', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (156, 4, '2025-12-13 16:39:57.370745', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (157, 2, '2025-12-13 16:44:34.52826', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (158, 2, '2025-12-13 16:50:29.363239', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (159, 4, '2025-12-13 16:50:54.747023', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (160, 2, '2025-12-13 16:51:33.352179', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (161, 2, '2025-12-13 16:58:27.209751', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (162, 2, '2025-12-13 16:59:42.812894', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (163, 2, '2025-12-13 20:47:44.853375', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (164, 2, '2025-12-13 20:50:42.780087', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (165, 2, '2025-12-13 20:57:52.329307', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (166, 2, '2025-12-13 20:59:08.614522', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (167, 2, '2025-12-13 21:00:10.722549', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (168, 2, '2025-12-13 21:03:33.069745', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (169, 2, '2025-12-13 21:06:01.645624', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (170, 2, '2025-12-13 21:09:34.843285', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (171, 2, '2025-12-13 21:12:12.326681', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (172, 2, '2025-12-13 21:13:38.452127', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (173, 2, '2025-12-13 21:14:15.616677', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (174, 2, '2025-12-13 21:15:38.815569', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (175, 2, '2025-12-13 21:16:40.764496', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (176, 2, '2025-12-13 21:17:21.552714', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (177, 2, '2025-12-13 21:27:36.328301', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (178, 5, '2025-12-13 21:27:55.018365', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (179, 2, '2025-12-13 21:30:30.245678', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (180, 2, '2025-12-13 21:35:26.946904', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (181, 2, '2025-12-13 21:36:52.703385', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (182, 2, '2025-12-13 21:40:40.328361', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (183, 2, '2025-12-13 21:41:29.020195', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (184, 2, '2025-12-13 21:45:30.997321', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (185, 2, '2025-12-13 21:46:41.544322', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (186, 2, '2025-12-13 21:48:40.148631', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (187, 2, '2025-12-13 21:49:04.605441', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (188, 2, '2025-12-13 21:49:24.148327', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (189, 2, '2025-12-13 21:49:49.618948', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (190, 2, '2025-12-13 21:50:14.530153', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (191, 2, '2025-12-13 21:50:34.485867', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (192, 2, '2025-12-13 21:51:35.581771', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (193, 2, '2025-12-13 21:52:21.59631', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (194, 2, '2025-12-13 21:53:30.893511', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (195, 2, '2025-12-13 21:54:05.909332', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (196, 3, '2025-12-13 21:55:01.145644', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (197, 2, '2025-12-13 22:00:02.38292', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (198, 2, '2025-12-13 22:03:35.49995', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (199, 2, '2025-12-13 22:07:40.887433', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (200, 2, '2025-12-13 22:16:24.356856', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (201, 2, '2025-12-13 22:19:05.100267', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (202, 2, '2025-12-13 22:21:02.789836', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (203, 2, '2025-12-13 22:21:53.671072', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (204, 2, '2025-12-13 22:23:28.064138', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (205, 5, '2025-12-13 22:24:03.243342', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (206, 2, '2025-12-13 22:26:09.460895', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (207, 2, '2025-12-13 22:30:29.74959', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (208, 2, '2025-12-13 22:32:01.18697', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (209, 2, '2025-12-13 22:36:11.866502', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (210, 2, '2025-12-13 22:38:04.727065', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (211, 2, '2025-12-13 22:40:41.203514', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (212, 2, '2025-12-13 22:41:14.003907', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (213, 4, '2025-12-13 22:41:44.213502', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (214, 2, '2025-12-13 22:46:54.622931', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (215, 2, '2025-12-13 22:50:07.681291', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (216, 2, '2025-12-13 22:54:44.294823', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (217, 2, '2025-12-13 23:01:12.639742', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (218, 2, '2025-12-13 23:05:27.417103', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (219, 3, '2025-12-13 23:07:32.447105', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (220, 1, '2025-12-13 23:17:08.036132', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (221, 2, '2025-12-13 23:18:12.075244', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (222, 2, '2025-12-13 23:18:52.051425', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (223, 2, '2025-12-13 23:35:56.227075', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (224, 2, '2025-12-13 23:49:01.635314', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (225, 2, '2025-12-13 23:53:26.519953', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (226, 2, '2025-12-13 23:54:18.087093', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (227, 2, '2025-12-13 23:57:49.175525', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (228, 2, '2025-12-14 00:02:38.702436', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (229, 1, '2025-12-14 00:04:56.742556', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (230, 1, '2025-12-14 00:05:03.23021', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (231, 2, '2025-12-14 00:05:56.792898', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (232, 2, '2025-12-14 00:10:38.653894', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (233, 2, '2025-12-14 00:17:42.931318', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (234, 2, '2025-12-14 09:30:37.532403', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (235, 2, '2025-12-14 09:34:50.575105', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (236, NULL, '2025-12-14 09:40:06.640375', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (237, 2, '2025-12-14 09:40:10.066669', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (238, NULL, '2025-12-14 09:43:47.592618', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (239, 2, '2025-12-14 09:43:50.670719', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (240, NULL, '2025-12-14 09:49:37.270899', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (241, 2, '2025-12-14 09:49:40.306969', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (242, NULL, '2025-12-14 09:54:25.917257', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (243, 2, '2025-12-14 09:54:28.83449', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (244, 2, '2025-12-14 09:56:16.756818', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (245, 2, '2025-12-14 09:58:12.196965', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (246, NULL, '2025-12-14 10:06:38.918524', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (247, 2, '2025-12-14 10:06:41.95091', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (248, NULL, '2025-12-14 10:12:51.973648', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (249, 2, '2025-12-14 10:12:54.971125', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (250, NULL, '2025-12-14 10:36:18.232433', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (251, 2, '2025-12-14 10:36:21.566292', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (252, 3, '2025-12-14 10:38:26.271932', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (253, NULL, '2025-12-14 10:38:41.383656', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (254, 2, '2025-12-14 10:38:43.955629', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (255, NULL, '2025-12-14 10:49:42.116334', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (256, 2, '2025-12-14 10:49:45.043377', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (257, NULL, '2025-12-14 10:55:42.451168', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (258, 2, '2025-12-14 10:55:45.340807', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (259, NULL, '2025-12-14 10:57:40.249477', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (260, 2, '2025-12-14 10:57:43.515862', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (261, 2, '2025-12-14 11:00:15.597721', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (262, NULL, '2025-12-14 11:02:33.927196', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (263, 2, '2025-12-14 11:02:36.724482', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (264, NULL, '2025-12-14 11:03:18.065146', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (265, 2, '2025-12-14 11:03:20.867586', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (266, NULL, '2025-12-14 11:12:45.603661', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (267, 2, '2025-12-14 11:12:48.380138', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (268, NULL, '2025-12-14 11:14:29.501664', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (269, 2, '2025-12-14 11:14:31.782118', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (270, NULL, '2025-12-14 11:17:44.410838', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (271, 2, '2025-12-14 11:17:47.230293', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (272, 3, '2025-12-14 11:18:27.832237', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (273, NULL, '2025-12-14 11:18:44.261572', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (274, 2, '2025-12-14 11:18:46.796063', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (275, NULL, '2025-12-14 11:19:41.955828', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (276, 2, '2025-12-14 11:19:44.389776', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (277, 5, '2025-12-14 11:20:07.228518', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (278, NULL, '2025-12-14 11:20:25.023151', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (281, 2, '2025-12-14 11:21:12.100105', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (279, 2, '2025-12-14 11:20:27.739753', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (280, NULL, '2025-12-14 11:21:09.502928', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (282, 3, '2025-12-14 11:21:26.822627', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (283, 5, '2025-12-14 11:21:49.229713', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (284, NULL, '2025-12-14 11:22:05.159971', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (285, 2, '2025-12-14 11:22:08.117569', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (286, 2, '2025-12-14 11:24:26.189996', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (287, 2, '2025-12-14 11:28:54.66967', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (288, 2, '2025-12-14 11:30:41.399471', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (289, 2, '2025-12-14 11:33:59.157411', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (290, 2, '2025-12-14 11:34:41.684602', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (291, 2, '2025-12-14 11:36:04.100651', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (292, 2, '2025-12-14 11:36:47.590213', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (293, 2, '2025-12-14 11:38:52.510796', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (294, 2, '2025-12-14 11:42:46.343892', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (295, 2, '2025-12-14 11:49:53.608181', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (296, 2, '2025-12-14 11:51:54.039468', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (297, 2, '2025-12-14 11:53:36.061987', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (298, 2, '2025-12-14 12:01:08.237686', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (299, 2, '2025-12-14 12:08:22.599554', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (300, 2, '2025-12-14 12:20:42.66306', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (301, 2, '2025-12-14 12:26:16.177895', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (302, 2, '2025-12-14 12:30:44.617522', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (303, 2, '2025-12-14 12:36:50.609106', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (304, 2, '2025-12-14 12:39:29.976542', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (305, 2, '2025-12-14 12:41:46.076601', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (306, 2, '2025-12-14 12:45:02.011906', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (307, 2, '2025-12-14 12:47:49.317823', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (308, 6, '2025-12-14 12:49:06.369003', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (309, 2, '2025-12-14 12:49:28.50356', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (310, 6, '2025-12-14 12:49:55.934425', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (311, 2, '2025-12-14 14:09:06.914417', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (312, 2, '2025-12-14 14:14:54.953143', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (313, 2, '2025-12-14 14:28:23.524222', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (314, 2, '2025-12-14 14:29:23.285349', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (315, 2, '2025-12-14 14:32:08.464771', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (316, 2, '2025-12-14 14:33:36.547947', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (317, NULL, '2025-12-14 14:37:12.732928', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (318, 2, '2025-12-14 14:37:17.424439', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (319, 2, '2025-12-14 14:41:05.01196', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (320, 2, '2025-12-14 14:43:17.603653', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (321, 2, '2025-12-14 14:48:36.121033', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (322, 2, '2025-12-14 14:51:00.378167', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (323, 2, '2025-12-14 14:55:38.645338', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (324, 2, '2025-12-14 15:00:06.249766', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (325, 2, '2025-12-14 15:03:02.848542', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (326, 2, '2025-12-14 15:03:59.734527', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (327, 2, '2025-12-14 15:05:23.792422', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (328, 2, '2025-12-14 15:06:12.367894', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (329, 2, '2025-12-14 15:11:17.218249', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (330, 2, '2025-12-14 15:17:02.368607', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (331, 2, '2025-12-14 15:21:13.537473', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (332, 2, '2025-12-14 15:27:14.584377', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (333, 2, '2025-12-14 15:30:37.721161', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (334, 2, '2025-12-14 15:32:14.184053', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (335, 2, '2025-12-14 15:41:19.925971', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (336, 2, '2025-12-14 15:42:42.95184', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (337, 2, '2025-12-14 15:43:46.53632', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (338, 2, '2025-12-14 15:45:05.111278', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (339, 2, '2025-12-14 15:45:41.968902', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (340, 2, '2025-12-14 15:45:57.248457', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (341, 2, '2025-12-14 15:46:35.373958', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (342, 2, '2025-12-14 15:47:48.848037', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (343, 2, '2025-12-14 15:49:26.67977', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (344, 2, '2025-12-14 15:50:48.594548', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (345, 2, '2025-12-14 15:53:07.690545', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (346, 2, '2025-12-14 15:54:25.152768', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (347, 2, '2025-12-14 15:55:24.206817', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (348, 2, '2025-12-14 15:55:52.339165', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (349, 2, '2025-12-14 15:59:16.706772', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (350, 2, '2025-12-14 16:01:18.978756', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (351, 2, '2025-12-14 16:05:51.116287', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (352, 2, '2025-12-14 16:07:37.758195', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (353, 2, '2025-12-14 16:08:13.78923', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (354, 2, '2025-12-14 16:11:27.45607', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (355, 2, '2025-12-14 16:12:53.645846', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (356, 2, '2025-12-14 16:17:10.511783', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (357, 2, '2025-12-14 16:18:10.236754', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (358, 2, '2025-12-14 16:20:09.808715', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (359, 2, '2025-12-14 16:32:23.588852', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (360, 2, '2025-12-14 16:33:38.713971', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (361, 2, '2025-12-14 16:34:00.467787', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (362, 2, '2025-12-14 16:35:16.878345', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (363, 2, '2025-12-14 16:35:49.436587', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (364, 2, '2025-12-14 16:36:54.786539', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (365, 2, '2025-12-14 16:41:15.78118', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (366, 2, '2025-12-14 16:47:02.57189', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (367, 2, '2025-12-14 16:47:43.272661', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (368, 2, '2025-12-14 16:48:47.749349', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (369, 2, '2025-12-14 16:50:18.060708', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (370, 2, '2025-12-14 16:52:45.883636', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (371, 2, '2025-12-14 16:55:40.597269', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (372, 2, '2025-12-14 16:59:26.26568', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (373, 2, '2025-12-14 17:04:12.316248', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (374, 2, '2025-12-14 17:07:31.50918', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (375, 2, '2025-12-14 17:15:40.832487', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (376, 2, '2025-12-14 17:15:49.381953', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (377, 2, '2025-12-14 17:15:52.126801', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (378, 6, '2025-12-14 17:16:11.160834', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (379, 2, '2025-12-14 17:23:08.48492', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (380, 2, '2025-12-14 17:26:57.916308', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (381, 2, '2025-12-14 17:31:24.221633', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (382, 2, '2025-12-14 17:32:41.064674', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (383, 2, '2025-12-14 17:36:13.405359', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (384, 2, '2025-12-14 17:38:26.957952', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (385, 2, '2025-12-14 17:41:29.795858', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (386, 2, '2025-12-14 17:42:04.508949', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (387, 2, '2025-12-14 17:42:35.340809', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (388, 2, '2025-12-14 17:43:21.056558', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (389, 2, '2025-12-14 17:44:15.515157', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (390, 2, '2025-12-14 17:47:12.540627', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (391, 2, '2025-12-14 17:49:11.44854', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (392, 2, '2025-12-14 17:54:01.70858', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (393, 2, '2025-12-14 17:56:03.465879', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (394, 2, '2025-12-14 18:15:53.266099', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (395, 2, '2025-12-14 18:20:58.069201', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (396, 2, '2025-12-14 18:33:58.199008', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (397, 2, '2025-12-14 18:36:40.959241', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (398, 2, '2025-12-14 18:39:21.307956', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (399, 2, '2025-12-14 20:19:19.648959', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (400, 4, '2025-12-14 20:22:55.812094', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (401, 2, '2025-12-14 20:24:23.651096', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (402, 2, '2025-12-14 20:35:59.74401', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (403, 2, '2025-12-14 20:37:24.281687', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (404, 2, '2025-12-14 20:37:57.481796', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (405, 2, '2025-12-14 20:52:35.667864', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (406, 2, '2025-12-14 20:53:31.553179', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (407, 2, '2025-12-14 20:54:04.887584', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (408, 2, '2025-12-14 20:55:57.85262', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (409, 2, '2025-12-14 20:57:12.321877', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (410, 2, '2025-12-14 20:58:15.456142', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (411, 2, '2025-12-14 20:59:20.54514', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (412, 2, '2025-12-14 21:01:11.985275', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (413, 2, '2025-12-14 21:01:54.756428', true);


ALTER TABLE public.record_signin ENABLE TRIGGER ALL;

--
-- TOC entry 5105 (class 0 OID 48120)
-- Dependencies: 229
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.report DISABLE TRIGGER ALL;

INSERT INTO public.report (reporter_id, reported_user_id, reason, reported_at, status) VALUES (2, 4, 'Ngôn từ khiếm nhã', '2025-12-13 12:23:41.286185', 0);
INSERT INTO public.report (reporter_id, reported_user_id, reason, reported_at, status) VALUES (2, 5, 'Scammer', '2025-12-13 23:01:29.196856', 0);


ALTER TABLE public.report ENABLE TRIGGER ALL;

--
-- TOC entry 5100 (class 0 OID 48077)
-- Dependencies: 224
-- Data for Name: request_password_reset; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.request_password_reset DISABLE TRIGGER ALL;



ALTER TABLE public.request_password_reset ENABLE TRIGGER ALL;

--
-- TOC entry 5098 (class 0 OID 48063)
-- Dependencies: 222
-- Data for Name: verify_email_change_token; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.verify_email_change_token DISABLE TRIGGER ALL;



ALTER TABLE public.verify_email_change_token ENABLE TRIGGER ALL;

--
-- TOC entry 5096 (class 0 OID 48049)
-- Dependencies: 220
-- Data for Name: verify_token; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.verify_token DISABLE TRIGGER ALL;



ALTER TABLE public.verify_token ENABLE TRIGGER ALL;

--
-- TOC entry 5133 (class 0 OID 0)
-- Dependencies: 240
-- Name: group_conversation_group_conversation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.group_conversation_group_conversation_id_seq', 19, true);


--
-- TOC entry 5134 (class 0 OID 0)
-- Dependencies: 244
-- Name: group_conversation_message_group_conversation_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5135 (class 0 OID 0)
-- Dependencies: 236
-- Name: private_conversation_message_private_conversation_message_i_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5136 (class 0 OID 0)
-- Dependencies: 233
-- Name: private_conversation_private_conversation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.private_conversation_private_conversation_id_seq', 16, true);


--
-- TOC entry 5137 (class 0 OID 0)
-- Dependencies: 227
-- Name: record_signin_record_signin_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.record_signin_record_signin_id_seq', 413, true);


--
-- TOC entry 5138 (class 0 OID 0)
-- Dependencies: 223
-- Name: request_password_reset_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.request_password_reset_request_id_seq', 1, false);


--
-- TOC entry 5139 (class 0 OID 0)
-- Dependencies: 249
-- Name: request_password_reset_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5140 (class 0 OID 0)
-- Dependencies: 217
-- Name: user_info_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_info_user_id_seq', 6, true);


--
-- TOC entry 5141 (class 0 OID 0)
-- Dependencies: 250
-- Name: verify_email_change_token_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5142 (class 0 OID 0)
-- Dependencies: 221
-- Name: verify_email_change_token_verification_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5143 (class 0 OID 0)
-- Dependencies: 251
-- Name: verify_token_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5144 (class 0 OID 0)
-- Dependencies: 219
-- Name: verify_token_verification_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.verify_token_verification_id_seq', 1, false);


-- Completed on 2025-12-14 21:15:08

--
-- PostgreSQL database dump complete
--


