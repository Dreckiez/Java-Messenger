--
-- PostgreSQL database dump
--


-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2025-12-20 10:02:37
--
-- TOC entry 5200 (class 0 OID 17211)
-- Dependencies: 220
-- Data for Name: user_info; Type: TABLE DATA; Schema: public; Owner: -
--

SET client_encoding = 'UTF-8';

SET SESSION AUTHORIZATION DEFAULT;

ALTER TABLE public.user_info DISABLE TRIGGER ALL;

INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (1, 'admin123', 'admin123@gmail.com', '$2a$10$vWiSkrRXXGhN3vb3aomXLe4xAG51UilgPrDH3y0yb67lTqVka3r4y', 1, 0, 'User', 'Super', '', NULL, '', true, false, true, '2025-12-12 22:57:54.037183', NULL, 0);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (3, 'bob123', 'bob123@fake.com', '$2a$10$WsnuMB3Ef3Qz1gMyQFSSBONsD8wRApFdpRB4WE.1.0W1ecn3T0cSW', 0, 0, 'Bob', 'Bob', '', NULL, NULL, true, false, true, '2025-12-12 23:00:20.714835', '2025-12-14 12:47:52.770783', 0);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (6, 'erik123', 'erik123@fake.com', '$2a$10$blisnUV4gYb1C9tXf84rceJp1w8u5AcKQcWe7AFdWaKoMdEZu8.oK', 0, 0, 'Erik', 'Kire', '', NULL, '', true, false, true, '2025-12-12 23:01:11.820903', '2025-12-14 12:50:03.964879', 2);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (5, 'dom123', 'dom123@fake.com', '$2a$10$HVVWfK5omrGIAV4K5ujm0.VTQgxzz58ILdROFSfe8Hms0r79dCnta', 0, 0, 'Dom', 'Cobb', 'dá', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765594951/avatars/dom123.jpg', true, false, true, '2025-12-12 23:00:54.459268', '2025-12-14 12:50:03.964879', 1);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (7, 'Dreckiez', 'kolienquan13@gmail.com', '$2a$10$7DKbQTUWlxOO5XaDAf2iO.qMbuu9tMSHeyPZRVr4iB6d8UI9gXzVG', 0, 1, 'Hung', 'Mike', '3 Tháng 2', '2005-04-13', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1763463925/avatars/Dreckiez.jpg', true, false, true, '2025-12-15 09:55:39.44053', '2025-12-20 09:38:06.415326', 1);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (2, 'alice123', 'alice123@fake.com', '$2a$10$yh6qoT7s236rrpFhRLvo7uaX6JLsCumKmXHljKuGMSHp9DbKjFZkK', 0, 0, 'Alice', 'In Borderland', '', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765685239/avatars/alice124.jpg', true, true, true, '2025-12-12 23:00:05.694802', '2025-12-20 09:56:38.541857', 1);
INSERT INTO public.user_info (user_id, username, email, hash_password, role, gender, first_name, last_name, address, birthday, avatar_url, is_active, is_online, is_accepted, joined_at, updated_at, friend_count) VALUES (4, 'charles123', 'charles123@fake.com', '$2a$10$XEj9gy65MOrbHi6H9CsNDeZjITuoQudps1zGCOXG/f2m3nbH3APDy', 0, 0, 'Charles', 'Catwright', '', NULL, 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1765619471/avatars/charles123.jpg', true, true, true, '2025-12-12 23:00:40.903627', '2025-12-20 09:58:15.851743', 1);


ALTER TABLE public.user_info ENABLE TRIGGER ALL;

--
-- TOC entry 5214 (class 0 OID 17382)
-- Dependencies: 234
-- Data for Name: block; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.block DISABLE TRIGGER ALL;

INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 5, '2025-12-13 11:23:52.955482', false, '2025-12-13 11:59:00.235679');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 6, '2025-12-13 11:59:36.874912', false, '2025-12-13 11:59:44.157401');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 5, '2025-12-14 10:13:04.55587', false, '2025-12-14 10:13:24.010582');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 11:14:58.578342', false, '2025-12-14 11:18:03.519032');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 11:19:47.328983', false, '2025-12-14 11:20:36.134326');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (2, 3, '2025-12-14 12:47:52.782418', false, '2025-12-14 12:48:50.695181');
INSERT INTO public.block (blocker_id, blocked_user_id, blocked_at, is_active, removed_at) VALUES (7, 2, '2025-12-20 01:19:13.530495', true, NULL);


ALTER TABLE public.block ENABLE TRIGGER ALL;

--
-- TOC entry 5222 (class 0 OID 17508)
-- Dependencies: 242
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
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (22, 'testnew', NULL, 7, 68, '2025-12-19 04:05:24.208414', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (20, 'Săn Loli', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1766171763/group_avatars/20.png', 2, 121, '2025-12-15 10:13:16.222431', false);
INSERT INTO public.group_conversation (group_conversation_id, group_name, avatar_url, owner_id, preview_message_id, created_at, is_encrypted) VALUES (21, 'Săn Milf', 'https://res.cloudinary.com/dh7nm63dk/image/upload/v1766171690/group_avatars/21.png', 7, 114, '2025-12-15 12:51:56.938624', false);


ALTER TABLE public.group_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5223 (class 0 OID 17524)
-- Dependencies: 243
-- Data for Name: delete_group_conversation; Type: TABLE DATA; Schema: public; Owner: -
--


--
-- TOC entry 5226 (class 0 OID 17560)
-- Dependencies: 246
-- Data for Name: group_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.group_conversation_message DISABLE TRIGGER ALL;

INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (1, 5, 2, 'Hello', '2025-12-14 20:19:40.91094', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (2, 5, 4, 'Hello', '2025-12-14 20:23:18.714591', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (13, 21, 7, 'DMM', '2025-12-19 04:16:11.957441', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (14, 22, 7, 'DMM', '2025-12-19 04:16:42.263688', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (18, 22, 7, 'e', '2025-12-19 05:04:30.855479', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (19, 21, 7, 'e', '2025-12-19 05:04:38.378498', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (25, 21, 2, 'huh', '2025-12-19 05:46:39.100856', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (26, 21, 2, 'ey', '2025-12-19 05:53:23.268591', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (27, 21, 7, 'huh', '2025-12-19 05:53:36.030886', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (28, 21, 2, 'm ko di bao ak', '2025-12-19 05:58:20.286836', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (29, 21, 7, 'nah', '2025-12-19 05:58:37.094138', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (30, 21, 2, 'val val val?', '2025-12-19 06:02:06.902162', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (31, 21, 2, 'thg cho nay', '2025-12-19 06:12:01.314924', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (32, 21, 2, 'rep bo coi', '2025-12-19 06:18:20.623471', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (33, 22, 2, 'cai deo j', '2025-12-19 06:18:46.749665', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (34, 22, 7, 'huh', '2025-12-19 06:19:00.849566', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (35, 22, 2, 'yo', '2025-12-19 06:28:51.164081', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (36, 22, 2, 'dick', '2025-12-19 06:29:25.893251', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (37, 21, 2, 'aa', '2025-12-19 06:29:34.075975', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (38, 22, 2, 'm beo', '2025-12-19 06:30:18.027953', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (39, 22, 2, 'beo vl', '2025-12-19 06:30:22.050396', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (40, 21, 2, 'asd', '2025-12-19 06:30:33.629925', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (41, 21, 2, 'a', '2025-12-19 06:33:57.022292', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (42, 21, 2, 'b', '2025-12-19 06:34:08.069232', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (43, 22, 2, 'asd', '2025-12-19 06:34:12.865942', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (44, 22, 7, 'dmm', '2025-12-19 06:56:32.742357', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (45, 22, 7, 'asd', '2025-12-19 06:56:56.821972', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (46, 22, 7, 'a', '2025-12-19 07:00:05.020946', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (47, 22, 7, 'a', '2025-12-19 07:07:07.071737', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (48, 22, 7, 'asd', '2025-12-19 07:07:09.851004', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (49, 22, 7, 'e', '2025-12-19 07:07:39.517703', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (50, 22, 7, 'a', '2025-12-19 07:08:50.385866', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (51, 22, 2, 'con cho', '2025-12-19 07:11:57.245272', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (52, 22, 2, 'asdkladsklasdasdfklj', '2025-12-19 07:16:16.544736', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (53, 21, 2, 'jklasdjklasdkjldas', '2025-12-19 07:16:23.053169', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (54, 20, 2, 'hola amigo', '2025-12-19 07:16:36.996455', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (55, 20, 7, 'bonjour mon ami', '2025-12-19 07:17:36.011795', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (56, 20, 2, 'konichiwa', '2025-12-19 07:21:56.244498', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (57, 20, 2, 'beo beo beo', '2025-12-19 07:23:18.12392', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (58, 21, 2, '36 beso', '2025-12-19 07:23:30.933277', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (59, 21, 2, 'a', '2025-12-19 07:28:19.700958', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (60, 20, 2, 'a', '2025-12-19 07:28:25.506489', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (61, 20, 2, 'e', '2025-12-19 07:34:54.668912', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (62, 21, 2, 'e', '2025-12-19 07:35:02.805103', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (63, 21, 2, 'w', '2025-12-19 07:39:34.82257', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (64, 20, 2, 'w', '2025-12-19 07:39:39.522488', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (65, 20, 2, 'e', '2025-12-19 07:41:14.445183', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (66, 21, 2, 'e', '2025-12-19 07:41:16.916055', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (67, 22, 2, 'w', '2025-12-19 07:41:23.912595', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (68, 22, 2, 'e', '2025-12-19 07:41:48.811555', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (69, 21, 2, 'e', '2025-12-19 07:41:52.47491', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (70, 20, 2, 'e', '2025-12-19 07:41:55.238964', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (71, 21, 2, '22', '2025-12-19 23:59:38.769292', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (72, 21, 7, '23', '2025-12-19 23:59:45.790759', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (73, 21, 2, '24', '2025-12-19 23:59:56.14303', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (74, 21, 2, '25', '2025-12-19 23:59:57.523239', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (75, 21, 2, '26', '2025-12-19 23:59:59.726216', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (76, 21, 2, '27', '2025-12-20 00:00:01.019137', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (77, 21, 7, '28', '2025-12-20 00:00:05.263157', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (78, 21, 7, '29', '2025-12-20 00:00:06.782295', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (79, 21, 7, '30', '2025-12-20 00:00:07.769297', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (80, 21, 7, '31', '2025-12-20 00:00:14.109364', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (81, 21, 2, '32', '2025-12-20 00:00:17.626025', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (82, 21, 2, '33', '2025-12-20 00:00:20.056697', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (83, 21, 2, '34', '2025-12-20 00:00:21.659289', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (84, 21, 2, '35', '2025-12-20 00:00:22.308966', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (85, 21, 7, 'thg cho 36', '2025-12-20 00:00:28.51384', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (86, 21, 7, '37', '2025-12-20 00:00:31.012698', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (87, 21, 7, '38', '2025-12-20 00:00:31.713065', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (88, 21, 7, '39', '2025-12-20 00:00:32.474416', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (89, 21, 7, '40', '2025-12-20 00:00:33.996934', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (90, 21, 2, 'woa woa con cho 41', '2025-12-20 00:00:44.005098', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (91, 21, 2, '42', '2025-12-20 00:00:46.380348', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (92, 21, 2, '43', '2025-12-20 00:00:47.001424', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (93, 21, 2, '44', '2025-12-20 00:00:47.722831', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (94, 21, 2, '45', '2025-12-20 00:00:49.649738', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (95, 21, 7, '46', '2025-12-20 00:00:51.404882', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (96, 21, 7, '47', '2025-12-20 00:00:52.364374', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (97, 21, 7, '48/', '2025-12-20 00:00:54.134819', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (98, 21, 7, '49', '2025-12-20 00:00:54.954292', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (99, 21, 7, '50', '2025-12-20 00:00:55.81668', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (100, 21, 2, 'test 51', '2025-12-20 00:01:06.323162', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (101, 21, 7, 'omg', '2025-12-20 00:26:25.416601', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (102, 21, 7, 'so gud', '2025-12-20 00:26:27.545883', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (103, 21, 7, 'luv luv', '2025-12-20 00:26:31.29836', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (104, 21, 4, 'cc jz', '2025-12-20 01:22:45.710086', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (105, 21, 7, 'huh', '2025-12-20 01:23:02.447209', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (106, 21, 4, 'm noi cc jv', '2025-12-20 01:23:11.767085', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (107, 21, 7, 't co noi j dou', '2025-12-20 01:23:19.009136', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (108, 21, 7, 'dm m', '2025-12-20 01:23:26.102293', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (109, 21, 7, 't kick', '2025-12-20 01:23:32.238704', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (110, 21, 2, 'chung m noi cai deo j v', '2025-12-20 01:58:07.622661', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (111, 21, 7, 'huh', '2025-12-20 01:58:27.146582', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (112, 21, 7, 't doi ten nhom', '2025-12-20 01:58:31.62028', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (113, 21, 2, 'the t doi avt', '2025-12-20 01:59:16.13586', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (114, 21, 7, 'ok', '2025-12-20 01:59:25.120357', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (115, 20, 7, 'con choa doi avt coi', '2025-12-20 02:15:14.952625', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (116, 20, 2, 'ok con cho', '2025-12-20 02:15:32.741762', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (117, 20, 2, 'dmm', '2025-12-20 05:07:20.085545', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (118, 20, 7, 'huh', '2025-12-20 05:07:24.88006', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (119, 20, 7, 'noi cc j v', '2025-12-20 05:07:28.231738', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (120, 20, 7, 'dmm', '2025-12-20 05:07:30.455162', NULL, 0);
INSERT INTO public.group_conversation_message (group_conversation_message_id, group_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (121, 20, 7, 'con choa', '2025-12-20 05:07:32.125482', NULL, 0);


ALTER TABLE public.group_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5227 (class 0 OID 17588)
-- Dependencies: 247
-- Data for Name: delete_group_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_group_conversation_message DISABLE TRIGGER ALL;



ALTER TABLE public.delete_group_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5216 (class 0 OID 17402)
-- Dependencies: 236
-- Data for Name: private_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.private_conversation DISABLE TRIGGER ALL;

INSERT INTO public.private_conversation (private_conversation_id, user1_id, user2_id, created_at, preview_message_id, user1_cleared_at, user2_cleared_at) VALUES (15, 2, 6, '2025-12-14 12:49:59.837677', NULL, NULL, NULL);
INSERT INTO public.private_conversation (private_conversation_id, user1_id, user2_id, created_at, preview_message_id, user1_cleared_at, user2_cleared_at) VALUES (16, 5, 6, '2025-12-14 12:50:03.977732', NULL, NULL, NULL);
INSERT INTO public.private_conversation (private_conversation_id, user1_id, user2_id, created_at, preview_message_id, user1_cleared_at, user2_cleared_at) VALUES (21, 4, 7, '2025-12-19 06:27:22.15926', 39, NULL, '2025-12-20 06:13:26.828967');


ALTER TABLE public.private_conversation ENABLE TRIGGER ALL;

--
-- TOC entry 5217 (class 0 OID 17425)
-- Dependencies: 237
-- Data for Name: delete_private_conversation; Type: TABLE DATA; Schema: public; Owner: -
--

--
-- TOC entry 5219 (class 0 OID 17443)
-- Dependencies: 239
-- Data for Name: private_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.private_conversation_message DISABLE TRIGGER ALL;

INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (4, 15, 2, 'Hi, this is user 2 in conversation 15', '2025-12-14 16:26:33.89448', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (5, 15, 6, 'Hello user 2, this is user 6', '2025-12-14 16:26:33.89448', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (6, 16, 5, 'Hi, this is user 5 in conversation 16', '2025-12-14 16:26:43.854', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (7, 16, 6, 'Hello user 5, this is user 6', '2025-12-14 16:26:43.854', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (27, 21, 4, 'dick', '2025-12-19 06:27:52.763535', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (29, 21, 7, 'me may beo', '2025-12-20 05:09:24.745242', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (30, 21, 4, 'o con choa nay', '2025-12-20 05:09:33.926707', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (31, 21, 4, 'djt me may', '2025-12-20 05:09:39.511893', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (32, 21, 7, 'm muon j', '2025-12-20 05:58:01.816421', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (33, 21, 4, '2 toi', '2025-12-20 05:58:10.489182', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (34, 21, 7, 'co deo ma cho', '2025-12-20 05:58:18.027294', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (35, 21, 4, 'deo co thi thoi', '2025-12-20 05:58:33.963308', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (36, 21, 7, 'ok', '2025-12-20 05:58:38.768517', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (37, 21, 7, 'con cac', '2025-12-20 06:13:21.572273', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (38, 21, 7, 'cc', '2025-12-20 06:13:22.684459', NULL, 0);
INSERT INTO public.private_conversation_message (private_conversation_message_id, private_conversation_id, sender_id, content, sent_at, updated_at, type) VALUES (39, 21, 7, 'ccc', '2025-12-20 06:13:23.83933', NULL, 0);


ALTER TABLE public.private_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5220 (class 0 OID 17489)
-- Dependencies: 240
-- Data for Name: delete_private_conversation_message; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.delete_private_conversation_message DISABLE TRIGGER ALL;



ALTER TABLE public.delete_private_conversation_message ENABLE TRIGGER ALL;

--
-- TOC entry 5207 (class 0 OID 17284)
-- Dependencies: 227
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.device DISABLE TRIGGER ALL;



ALTER TABLE public.device ENABLE TRIGGER ALL;

--
-- TOC entry 5228 (class 0 OID 17630)
-- Dependencies: 248
-- Data for Name: encryption_group; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.encryption_group DISABLE TRIGGER ALL;



ALTER TABLE public.encryption_group ENABLE TRIGGER ALL;

--
-- TOC entry 5213 (class 0 OID 17363)
-- Dependencies: 233
-- Data for Name: friend; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.friend DISABLE TRIGGER ALL;

INSERT INTO public.friend (user_id1, user_id2, made_friend_at) VALUES (2, 6, '2025-12-14 12:49:59.82216');
INSERT INTO public.friend (user_id1, user_id2, made_friend_at) VALUES (5, 6, '2025-12-14 12:50:03.96368');
INSERT INTO public.friend (user_id1, user_id2, made_friend_at) VALUES (4, 7, '2025-12-19 06:27:22.137245');


ALTER TABLE public.friend ENABLE TRIGGER ALL;

--
-- TOC entry 5212 (class 0 OID 17343)
-- Dependencies: 232
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
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 4, 0, '2025-12-14 16:51:51.195998', NULL, true);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 2, 1, '2025-12-15 09:59:22.461362', '2025-12-15 10:08:54.737297', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 2, 3, '2025-12-15 10:09:42.640446', '2025-12-15 10:09:44.373142', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 2, 3, '2025-12-15 10:09:45.737235', '2025-12-15 10:10:21.772708', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 2, 3, '2025-12-15 10:10:40.131033', '2025-12-15 10:10:45.918501', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:29:50.38198', '2025-12-17 00:29:52.935133', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 2, 1, '2025-12-15 10:12:23.964455', '2025-12-15 10:12:31.207408', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 4, 3, '2025-12-15 09:59:37.722187', '2025-12-15 12:49:12.065933', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (7, 4, 1, '2025-12-15 12:50:41.285771', '2025-12-15 12:51:38.237966', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-15 14:12:58.928844', '2025-12-15 14:13:32.801812', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:32:55.857288', '2025-12-17 00:33:01.495492', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-15 14:15:57.870686', '2025-12-15 15:12:10.527224', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-15 15:12:18.19243', '2025-12-15 15:12:47.394603', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-15 15:12:49.873207', '2025-12-15 15:13:03.875749', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (4, 7, 3, '2025-12-17 00:34:45.614663', '2025-12-17 00:34:50.295586', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-15 15:13:06.023261', '2025-12-16 16:28:22.528922', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 16:30:15.071915', '2025-12-16 16:30:28.267793', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 16:30:37.661137', '2025-12-16 16:31:51.913804', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 5, 3, '2025-12-14 16:51:44.780598', '2025-12-16 16:31:53.346351', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 16:32:58.438779', '2025-12-16 16:33:13.628972', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:21:38.010565', '2025-12-16 17:21:48.542969', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:21:55.667436', '2025-12-16 17:22:09.802567', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:22:18.590375', '2025-12-16 17:22:25.619809', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:22:26.947224', '2025-12-16 17:47:42.189137', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:48:02.841977', '2025-12-16 17:48:16.934453', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 17:48:19.014917', '2025-12-16 19:19:48.003137', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 19:43:13.391081', '2025-12-16 19:43:18.28925', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 19:43:42.258131', '2025-12-16 19:50:22.635639', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 19:50:28.688386', '2025-12-16 19:50:31.384507', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 20:05:51.361375', '2025-12-16 20:05:58.225231', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 21:27:35.47821', '2025-12-16 21:27:40.263562', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (4, 7, 3, '2025-12-17 00:34:53.930963', '2025-12-17 00:34:56.845874', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 21:27:51.507521', '2025-12-16 21:28:11.213309', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 22:22:25.470136', '2025-12-16 22:22:28.144545', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 22:30:15.334417', '2025-12-16 22:30:18.444704', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 22:32:27.284694', '2025-12-16 22:32:30.372113', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 22:37:23.969969', '2025-12-16 22:44:59.825291', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 22:57:32.437601', '2025-12-16 23:00:16.159254', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:01:43.775773', '2025-12-16 23:02:24.295752', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:07:29.005024', '2025-12-16 23:07:51.803294', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:09:27.237127', '2025-12-16 23:09:50.617035', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:11:03.758781', '2025-12-16 23:11:11.486096', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:32:29.692606', '2025-12-16 23:32:41.412813', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:41:49.749744', '2025-12-16 23:41:57.595036', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-16 23:52:07.751855', '2025-12-16 23:52:16.771349', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:12:21.922415', '2025-12-17 00:12:33.822314', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:15:57.265971', '2025-12-17 00:16:02.79329', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:20:13.913869', '2025-12-17 00:20:16.899001', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:21:43.051927', '2025-12-17 00:21:44.668131', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:23:10.373711', '2025-12-17 00:24:47.605916', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 3, '2025-12-17 00:28:56.819526', '2025-12-17 00:29:46.939654', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (2, 7, 1, '2025-12-17 00:33:04.654815', '2025-12-17 00:35:37.129292', false);
INSERT INTO public.friend_request (sender_id, receiver_id, status, sent_at, updated_at, is_active) VALUES (4, 7, 1, '2025-12-17 00:35:01.307527', '2025-12-19 06:27:22.167156', false);


ALTER TABLE public.friend_request ENABLE TRIGGER ALL;

--
-- TOC entry 5224 (class 0 OID 17541)
-- Dependencies: 244
-- Data for Name: group_conversation_member; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.group_conversation_member DISABLE TRIGGER ALL;

INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (1, 2, '2025-12-12 23:11:57.464653', '2025-12-12 23:11:57.464653', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (1, 3, '2025-12-12 23:11:57.471174', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (1, 5, '2025-12-12 23:11:57.47827', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (1, 6, '2025-12-12 23:11:57.482351', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (2, 5, '2025-12-12 23:12:03.436497', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (2, 6, '2025-12-12 23:12:03.440046', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (3, 2, '2025-12-12 23:12:09.487804', '2025-12-12 23:12:09.487804', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (3, 3, '2025-12-12 23:12:09.491343', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (3, 4, '2025-12-12 23:12:09.494347', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (4, 4, '2025-12-12 23:12:16.212987', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (4, 6, '2025-12-12 23:12:16.215528', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (5, 2, '2025-12-12 23:12:22.668718', '2025-12-12 23:12:22.668718', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (5, 4, '2025-12-12 23:12:22.674236', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (1, 4, '2025-12-12 23:11:57.475288', NULL, 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (5, 6, '2025-12-12 23:12:22.677235', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (2, 3, '2025-12-12 23:12:03.433408', NULL, 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (4, 3, '2025-12-12 23:12:16.208981', NULL, 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (6, 2, '2025-12-13 23:09:40.6304', '2025-12-13 23:09:40.6304', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (6, 3, '2025-12-13 23:09:40.646627', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (6, 5, '2025-12-14 00:17:48.632422', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (7, 2, '2025-12-14 09:35:19.387248', '2025-12-14 09:35:19.387248', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (7, 3, '2025-12-14 09:35:19.396587', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (7, 5, '2025-12-14 09:35:19.400595', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (8, 2, '2025-12-14 09:41:19.972607', '2025-12-14 09:41:19.972607', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (8, 4, '2025-12-14 09:41:19.975869', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (9, 2, '2025-12-14 09:44:26.981835', '2025-12-14 09:44:26.981835', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (9, 5, '2025-12-14 09:44:26.985346', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (10, 2, '2025-12-14 09:50:07.597768', '2025-12-14 09:50:07.597768', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (10, 3, '2025-12-14 09:50:07.601283', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (11, 2, '2025-12-14 09:54:50.418831', '2025-12-14 09:54:50.418831', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (11, 3, '2025-12-14 09:54:50.42194', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (12, 2, '2025-12-14 09:55:48.531678', '2025-12-14 09:55:48.531678', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (12, 3, '2025-12-14 09:55:48.534698', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (13, 2, '2025-12-14 09:58:18.568175', '2025-12-14 09:58:18.568175', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (13, 5, '2025-12-14 09:58:18.5737', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (14, 2, '2025-12-14 10:07:00.079849', '2025-12-14 10:07:00.079849', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (14, 3, '2025-12-14 10:07:00.084888', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (15, 2, '2025-12-14 10:43:00.198488', '2025-12-14 10:43:00.198488', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (15, 3, '2025-12-14 10:43:00.205528', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (16, 2, '2025-12-14 10:43:14.72648', '2025-12-14 10:43:14.72648', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (16, 3, '2025-12-14 10:43:14.730836', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (17, 2, '2025-12-14 11:14:52.435515', '2025-12-14 11:14:52.435515', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (17, 3, '2025-12-14 11:14:52.439518', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (18, 6, '2025-12-14 12:50:29.017825', '2025-12-14 12:50:29.017825', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (18, 2, '2025-12-14 12:50:29.020062', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (19, 6, '2025-12-14 12:50:44.067408', '2025-12-14 12:50:44.067408', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (19, 5, '2025-12-14 12:50:44.070917', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (20, 2, '2025-12-15 10:26:48.803955', NULL, 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (22, 7, '2025-12-19 04:05:24.214421', '2025-12-19 04:05:24.214421', 1, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (21, 4, '2025-12-20 03:08:02.726251', NULL, 0, NULL);
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (21, 7, '2025-12-20 03:21:35.59023', NULL, 1, '2025-12-20 05:59:04.956593');
INSERT INTO public.group_conversation_member (group_conversation_id, member_id, joined_at, appointed_at, group_role, history_cleared_at) VALUES (20, 7, '2025-12-19 04:04:28.294033', NULL, 0, '2025-12-20 06:13:35.857949');


ALTER TABLE public.group_conversation_member ENABLE TRIGGER ALL;

--
-- TOC entry 5208 (class 0 OID 17296)
-- Dependencies: 228
-- Data for Name: record_online_user; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.record_online_user DISABLE TRIGGER ALL;

INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('dfdd537d-562c-6ad6-289a-20295d1a3840', 7, '2025-12-16 17:21:06.651714', '2025-12-16 17:22:33.840502');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('682d63ad-209d-9a64-73b9-22568f0bbf3a', 2, '2025-12-16 17:21:29.43029', '2025-12-16 17:22:34.329301');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d055b350-190b-5b17-5c14-5e25e7d1fdfd', 7, '2025-12-16 17:47:59.133786', '2025-12-16 17:48:15.771851');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6a9bd9c4-8593-3e86-e93a-57a72d13456b', 7, '2025-12-16 17:48:34.419376', '2025-12-16 17:48:37.13325');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('28214d26-df6b-2402-5259-d722b5bf64d2', 2, '2025-12-16 17:47:37.180769', '2025-12-16 17:48:39.213199');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('cf3a781f-5495-6222-c4b8-9bcda6ef4871', 2, '2025-12-16 19:19:29.685141', '2025-12-16 19:29:09.512907');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('ec347ea0-14f3-74cf-c365-b038a94380d4', 7, '2025-12-16 19:19:42.545214', '2025-12-16 19:29:11.527159');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('67a99e97-3280-7239-03ef-00060c4a8c15', 2, '2025-12-16 19:42:44.640395', '2025-12-16 19:48:29.153415');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('68adeb85-1357-f76b-1e03-536967482d7d', 7, '2025-12-16 19:43:07.431298', '2025-12-16 19:48:30.516573');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('79b879f3-cb77-9bbf-e762-75dae4763a79', 2, '2025-12-16 19:50:06.391935', '2025-12-16 20:03:53.631282');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b58f252f-8543-5345-f581-670b2bd7fe49', 7, '2025-12-16 19:49:48.208709', '2025-12-16 20:03:55.363353');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('94ebe77a-f0ec-20ff-1c40-435293dbb5ac', 7, '2025-12-16 20:05:17.272276', '2025-12-16 20:08:11.682123');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c14f3d74-c4ff-4f84-8bc6-c669beb4823b', 2, '2025-12-16 20:05:37.023857', '2025-12-16 20:08:13.061726');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d78e3265-30bf-eef4-aef3-44c67c2e2656', 2, '2025-12-16 21:27:16.173778', '2025-12-16 21:28:16.834477');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('90e41f1f-ac73-0e8b-5d12-76150d7425c7', 7, '2025-12-16 21:26:54.675602', '2025-12-16 21:28:17.89298');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('76c77e9a-580b-d03d-259d-1f6639bbec19', 7, '2025-12-16 22:22:03.645137', '2025-12-16 22:24:08.824568');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('32396b12-abab-4f4f-e520-0d7efe290647', 2, '2025-12-16 22:22:16.694117', '2025-12-16 22:24:09.36517');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1fff90e1-a06d-7731-d15e-a5de9c83e64c', 7, '2025-12-16 22:28:11.490461', '2025-12-16 22:31:13.640994');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('15cdee0c-5099-aa01-fdef-018dcfe24c32', 2, '2025-12-16 22:28:22.334768', '2025-12-16 22:31:14.888747');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('4cbac60b-98f3-4700-b411-fa1b7e24d687', 7, '2025-12-16 22:32:01.501395', '2025-12-16 22:32:35.574354');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('534c85e5-fa9f-d090-87ab-d3a140764202', 2, '2025-12-16 22:32:17.973968', '2025-12-16 22:32:36.098058');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('40f1cfb8-721d-9350-788e-2627a42cfc26', 7, '2025-12-16 22:37:12.483025', '2025-12-16 22:42:32.198892');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('47bd1939-c4bc-8079-a822-b61121c9a663', 2, '2025-12-16 22:36:58.594985', '2025-12-16 22:42:35.230362');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0ecfcf61-50c7-1b65-5d22-3af60c561b98', 7, '2025-12-16 22:43:17.433985', '2025-12-16 22:44:15.323218');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('24f06c58-6314-fbf1-d9e0-a4f0d7d7529e', 2, '2025-12-16 22:43:34.731933', '2025-12-16 22:44:16.66021');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('bc4119b6-df64-9b2a-4806-c46db4ffd0fa', 7, '2025-12-16 22:44:47.478242', '2025-12-16 22:45:12.361075');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d4dbacc5-89a0-25b5-d8eb-97c38acdaad5', 2, '2025-12-16 22:44:37.62937', '2025-12-16 22:45:13.337139');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('80e756e1-62a5-2a5b-ee1a-0ab52cc4d4c3', 7, '2025-12-16 22:56:58.677849', '2025-12-16 22:58:29.514453');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b93d5174-d894-c101-77dc-ac319c1e8a24', 2, '2025-12-16 22:57:19.133456', '2025-12-16 22:58:30.927318');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('2cd055b0-f0d2-db78-cdd2-734643e5df94', 7, '2025-12-16 22:58:56.784754', '2025-12-16 22:59:27.566782');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3e057a1f-a828-c78a-ac69-61f3f5edec21', 7, '2025-12-16 22:59:39.701477', '2025-12-16 23:01:01.495079');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a7e058a5-ee26-4da5-74d4-a2b06787340c', 2, '2025-12-16 23:00:09.882614', '2025-12-16 23:01:02.938973');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f432ac8d-3a90-6f0a-36e5-4dbaf350c588', 2, '2025-12-16 23:01:31.729427', '2025-12-16 23:02:25.889187');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('66bddd29-8a8b-05ba-40db-79b2a9c2e226', 7, '2025-12-16 23:01:23.746667', '2025-12-16 23:02:26.313675');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('bb990404-1e88-2e70-20d4-121ea3f9cde0', 7, '2025-12-16 23:03:06.740495', '2025-12-16 23:03:31.8639');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c9e20788-566f-a830-3c85-09fb86ec37a1', 7, '2025-12-16 23:07:02.051124', '2025-12-16 23:08:42.800025');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c0b63492-e950-0bce-4f0b-c7b456743976', 2, '2025-12-16 23:07:20.405152', '2025-12-16 23:08:45.010875');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('69c422cf-d218-e460-623a-ce5535254bf9', 7, '2025-12-16 23:09:19.606451', '2025-12-16 23:10:00.366194');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('11102352-e8a6-a868-fff5-ac5a83cdbf23', 2, '2025-12-16 23:09:12.823264', '2025-12-16 23:10:02.424896');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6ab1b473-3a3c-e5e5-d190-e1ac9204ddd7', 7, '2025-12-16 23:10:41.558632', '2025-12-16 23:11:24.865781');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a836cc38-ba87-53c6-556d-d6c51d247134', 7, '2025-12-16 23:32:25.976493', '2025-12-16 23:32:40.290657');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('9e801da3-a73b-12be-025c-9b243cac4b1c', 2, '2025-12-16 23:10:58.798247', '2025-12-16 23:42:10.77723');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('bc013ed6-96f6-7eb2-6e3a-94b16603d5cf', 7, '2025-12-16 23:41:47.675767', '2025-12-16 23:42:12.961988');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('50fbab00-7bf5-4a76-0358-524ad434dc58', 7, '2025-12-16 23:51:31.18868', '2025-12-16 23:52:32.460636');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('13d1668b-b4b0-1041-f64d-bc53343b7a12', 2, '2025-12-16 23:51:56.346265', '2025-12-16 23:52:34.522291');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8f0c64e4-799b-21aa-6e3a-127d305615af', 7, '2025-12-17 00:11:58.953945', '2025-12-17 00:15:40.12211');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8fb5421c-9e8f-71c9-4db3-142eb3817528', 7, '2025-12-17 00:15:54.13389', '2025-12-17 00:19:55.767722');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('fbdd46e1-f3b1-a12f-4c83-8a6b0e1c74c6', 7, '2025-12-17 00:20:11.002526', '2025-12-17 00:21:28.034672');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e53b7ca2-7cdf-aa1c-4033-dfd709ead9e7', 7, '2025-12-17 00:21:40.023487', '2025-12-17 00:21:58.354858');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('2897577e-b82a-acba-99a1-c19bcf0a2da1', 2, '2025-12-17 00:12:16.549205', '2025-12-17 00:21:59.56617');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('36d837ac-7e2a-fbbd-a459-9a196121df8c', 2, '2025-12-17 00:23:06.207294', '2025-12-17 00:23:59.918775');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('23b748ac-7cc7-13ab-eca1-8451e4fb3e17', 7, '2025-12-17 00:22:51.946969', '2025-12-17 00:24:02.894999');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f797b73f-5426-a3f6-ca58-4042d18354ee', 7, '2025-12-17 00:24:40.420836', '2025-12-17 00:28:42.213923');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('80db3277-f3af-8c50-3fb6-f55cbf6f07b9', 7, '2025-12-17 00:28:54.499607', '2025-12-17 00:29:16.216582');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f6a38670-71a3-0170-76e0-583ecf0fee55', 7, '2025-12-17 00:29:43.564466', '2025-12-17 00:29:55.334799');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('df1d7ca0-5242-7c6a-ff71-d14462bb5826', 2, '2025-12-17 00:24:27.443654', '2025-12-17 00:29:56.374661');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('ebb52dab-e720-9583-1a92-5925d5123373', 7, '2025-12-17 00:32:28.470223', '2025-12-17 00:35:00.67827');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('7ea33cc2-1f74-5cd2-b6ea-284f4fcd23fc', 7, '2025-12-17 00:35:18.186684', '2025-12-17 00:36:18.226592');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1aeebf02-3505-48c0-356c-c818a1f35720', 4, '2025-12-17 00:34:34.239377', '2025-12-17 00:36:34.847972');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d2d6b98f-8ca1-4a84-60d8-5758b9ce2872', 2, '2025-12-17 00:32:45.739244', '2025-12-17 00:37:15.043392');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('9b0e7432-ec18-6d09-e5a0-1a37c6b62671', 7, '2025-12-17 00:36:52.561819', '2025-12-17 00:37:16.11011');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('89a35c4a-f01c-8903-4ea2-3415e82971f1', 2, '2025-12-17 01:18:28.734103', '2025-12-17 01:19:03.474039');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('cf76c2f5-1e8b-465e-7d01-379a01af70eb', 7, '2025-12-17 01:18:01.092553', '2025-12-17 01:19:04.288382');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('2f2ca29b-c30b-a3d7-95e5-7edd02320318', 2, '2025-12-17 01:26:36.376843', '2025-12-17 01:32:30.848805');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('5876762c-7aee-b17d-2bd9-65d3aa047cde', 2, '2025-12-17 01:34:42.602099', '2025-12-17 01:41:14.261609');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d11d3990-fe14-db9a-12f0-01e25001eead', 2, '2025-12-17 01:41:32.114716', '2025-12-17 01:42:16.444542');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('78ebe78c-49d5-a051-d956-dba06fdda220', 2, '2025-12-17 01:42:36.854428', '2025-12-17 01:42:42.556443');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d4d8e68a-636a-4349-c6a3-1b5933d229cd', 7, '2025-12-17 01:42:03.578461', '2025-12-17 01:42:46.699921');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6d631587-747e-8d96-927d-ffdbd56fc8d4', 7, '2025-12-17 13:48:36.42648', '2025-12-17 13:48:40.866431');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a6021384-a10c-7faa-164a-2e8e0b1c7564', 7, '2025-12-17 13:48:16.763796', '2025-12-17 13:48:40.872222');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f1d9d6c6-d106-26f3-4509-e7f136ff4679', 2, '2025-12-17 13:48:09.590921', '2025-12-17 13:48:41.632434');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f8fc591b-6bac-883f-6e4b-c922ccb577e7', 7, '2025-12-17 14:05:02.005194', '2025-12-17 14:06:00.771797');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8016082d-730b-d581-f9ba-339e9da68504', 7, '2025-12-17 14:06:36.848102', '2025-12-17 14:09:39.277178');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('4153837d-aa8f-00a8-b587-f8536af34998', 7, '2025-12-17 14:09:51.635974', '2025-12-17 14:10:45.729978');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a4f68a5e-21a3-fa61-2a95-02eb2495ea91', 7, '2025-12-17 14:11:18.377241', '2025-12-17 14:13:14.259945');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1487a70d-eb68-3c88-b2df-d8d1a31498dd', 7, '2025-12-17 14:14:33.76152', '2025-12-17 14:14:46.783173');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('85ceea8b-b3a2-6a91-f5ef-f539ba01442d', 7, '2025-12-17 14:13:48.234597', '2025-12-17 14:14:46.784177');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('444f0c2e-cedf-dff2-7d38-4da36cab00ca', 7, '2025-12-17 14:57:13.543579', '2025-12-17 14:57:28.515466');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0b2d2165-b240-b436-2485-8cb89c223776', 7, '2025-12-17 15:21:21.531988', '2025-12-17 15:24:06.041999');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1506d1d7-556a-1d95-807c-74040f09a37c', 7, '2025-12-17 15:47:21.566812', '2025-12-17 15:49:44.649975');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0e76bb0e-98ac-d1bd-e5ba-6e5b5c7f43b6', 7, '2025-12-17 15:50:25.537677', '2025-12-17 15:52:16.612717');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e44da522-c6df-bacf-120b-dd8f42ecd6a7', 7, '2025-12-17 15:52:27.746272', '2025-12-17 15:53:05.233277');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('be862d33-1429-4422-0c85-ef7c7807c3af', 7, '2025-12-17 15:53:54.304948', '2025-12-17 15:56:15.316272');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('217c47e7-68c9-515b-49b6-01f47920cc7b', 7, '2025-12-17 15:53:36.608442', '2025-12-17 15:56:15.318801');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b87e5488-b28f-da98-7f93-4ea0ed1c70c0', 2, '2025-12-17 15:54:50.659549', '2025-12-17 15:56:13.142884');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f58eabf5-3206-646c-6371-923abcd4d348', 2, '2025-12-17 15:57:55.949206', '2025-12-17 16:01:22.972207');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('256c4c8e-6831-94e1-c283-eefb77aace15', 7, '2025-12-17 15:57:46.136688', '2025-12-17 16:01:22.987528');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('2056768a-e490-db7a-6946-064588dd393d', 2, '2025-12-17 16:02:12.880749', '2025-12-17 16:05:55.684535');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1a059a26-eeac-1847-974e-1724513cc421', 7, '2025-12-17 16:02:04.854466', '2025-12-17 16:05:57.46826');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0b19f7a4-2138-bcce-41a4-b6517b5b3f71', 7, '2025-12-17 16:17:06.813046', '2025-12-17 16:25:07.580602');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f1a2d7ed-65d0-558c-1e59-5e540293e18e', 2, '2025-12-17 16:17:49.116976', '2025-12-17 16:25:09.370121');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('31012f2e-11f2-6cc4-e653-fd0853935359', 7, '2025-12-17 16:25:30.409085', '2025-12-17 16:26:15.928586');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('673e5801-2085-1817-3d6a-d37da56ca561', 2, '2025-12-17 16:30:46.99656', '2025-12-17 16:31:00.088463');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('7ab5c69f-886d-3d4a-8537-7e17e2242f16', 7, '2025-12-17 16:29:20.230865', '2025-12-17 16:31:01.032908');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8ab3a053-fe40-3e3b-0392-237eee771504', 2, '2025-12-17 16:34:15.898558', '2025-12-17 16:40:01.772629');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('abb6e5aa-4d6e-196f-016a-08a2d34b1307', 7, '2025-12-17 16:33:58.314713', '2025-12-17 16:40:02.750678');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b92604ff-c046-33d6-29de-b16bef2cea2e', 2, '2025-12-19 02:34:30.237561', '2025-12-19 02:37:38.302098');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('777c00d8-ad30-7a96-5261-91ae1718915a', 7, '2025-12-19 02:33:37.815079', '2025-12-19 02:37:39.049402');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('88c82336-9612-96d2-1520-c4a36dcfdf8b', 7, '2025-12-19 02:38:01.360128', '2025-12-19 02:39:18.58675');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3981b048-c381-0aea-a7ca-6a5ae8a57791', 2, '2025-12-19 02:38:25.741125', '2025-12-19 02:39:18.599463');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('39022cfa-f10a-208f-b61a-d69a222f006e', 4, '2025-12-19 02:57:08.899111', '2025-12-19 02:57:14.197168');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('69d942d6-2570-932d-b977-63381d0adf8c', 2, '2025-12-19 02:54:13.306261', '2025-12-19 02:57:16.500062');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('82cbedf5-6cf6-9f89-54e0-e55f43254b23', 7, '2025-12-19 02:53:26.610877', '2025-12-19 02:57:17.826601');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('eb803ba0-906f-5ead-4518-3bc21ad1aa8e', 7, '2025-12-19 04:02:18.839137', '2025-12-19 04:02:41.586301');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b048695c-9752-b06d-e89b-11108f7e78c9', 2, '2025-12-19 04:03:33.182266', '2025-12-19 04:05:54.215528');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('bd80b5ec-01e4-f5a2-ceba-10dfea762219', 7, '2025-12-19 04:03:12.44766', '2025-12-19 04:06:00.715164');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('cdf9f133-ae08-2250-dd19-7a90785d93e0', 7, '2025-12-19 04:07:10.727447', '2025-12-19 04:17:50.029201');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('2ca36601-afe0-ae1f-c6d1-8356eb404fe7', 7, '2025-12-19 04:37:08.405549', '2025-12-19 04:38:08.580761');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3a3b35e4-3db2-95f6-9fa1-6ebf2a6fb668', 7, '2025-12-19 04:39:23.483977', '2025-12-19 04:41:13.38053');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('df16fd7c-8564-2fa8-07ee-4276e7131706', 7, '2025-12-19 04:41:23.810459', '2025-12-19 04:42:22.527084');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e32e7723-da51-5c13-4300-4cf8f6df44f3', 7, '2025-12-19 04:42:33.637061', '2025-12-19 04:42:46.936209');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('87422de8-7919-812e-c3f3-54c1aa763bb5', 7, '2025-12-19 04:56:13.151383', '2025-12-19 04:56:39.593101');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3038bcc7-8427-9bd2-6e46-c995cbc61541', 7, '2025-12-19 05:01:13.623528', '2025-12-19 05:02:08.156336');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('32ae2872-8def-6ad0-331c-25ef6752c262', 2, '2025-12-19 05:05:09.988373', '2025-12-19 05:05:55.91269');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6c344dac-bb34-5dec-6151-05d2cdf861c0', 7, '2025-12-19 05:04:26.823695', '2025-12-19 05:05:57.566102');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8d02e792-36ff-7512-340b-aef4716129fe', 2, '2025-12-19 05:33:06.874163', '2025-12-19 05:35:23.150141');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6a30d3ec-5490-074d-b529-0825c31924d1', 7, '2025-12-19 05:32:44.313612', '2025-12-19 05:35:23.570116');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f146be01-63ce-a63e-1b21-87fd8704014b', 2, '2025-12-19 05:46:24.935057', '2025-12-19 05:52:33.287814');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8c374de5-2d03-d55b-fa63-b29e9f1fcfb9', 7, '2025-12-19 05:45:57.231446', '2025-12-19 05:52:34.628724');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('af885de7-cb51-f30c-d709-4e4d37c7153a', 2, '2025-12-19 05:53:04.521742', '2025-12-19 05:54:06.678835');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('44945024-c27b-546a-6de9-b34f27c4ef46', 7, '2025-12-19 05:52:49.007641', '2025-12-19 05:54:23.247139');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e4226c2a-a7de-9a40-ce61-4c4d58772175', 7, '2025-12-19 05:57:08.156383', '2025-12-19 05:57:16.623586');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('262aeb5c-0035-bc1e-e4ff-aa0fde719337', 2, '2025-12-19 05:58:10.418364', '2025-12-19 06:00:31.657949');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3e215a2a-7b41-f861-1d14-8fe7899abbdf', 7, '2025-12-19 05:57:45.048137', '2025-12-19 06:00:32.801365');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b8967792-52af-46de-e245-0507728f81b2', 7, '2025-12-19 06:01:26.537453', '2025-12-19 06:02:16.808666');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a5c3ea1f-072b-cc69-f65d-cb2a9d972de2', 2, '2025-12-19 06:01:47.807511', '2025-12-19 06:02:17.958486');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('5a0cdbeb-82ed-5dc8-e280-18c06261b2b5', 7, '2025-12-19 06:11:15.852071', '2025-12-19 06:14:06.22275');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('61d48319-89de-ff0f-526a-61ae2b2a7ffd', 2, '2025-12-19 06:11:53.333555', '2025-12-19 06:14:07.34113');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('49ced16c-f3ea-ed72-c62e-c6a049b6caa2', 2, '2025-12-19 06:18:12.228886', '2025-12-19 06:19:05.00997');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b761fc27-2a9c-5f93-2e6f-140a078f595e', 7, '2025-12-19 06:17:57.065956', '2025-12-19 06:19:06.185019');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d94488aa-3a8d-5c9a-06b2-9d85fe588317', 7, '2025-12-19 06:27:06.305572', '2025-12-19 06:28:12.012106');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6b0170b5-dc6d-55b4-1231-6fd7e0fe2e7e', 2, '2025-12-19 06:27:17.36512', '2025-12-19 06:28:12.608586');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f9f0443b-7ff4-c98c-f621-dc4c781b0339', 4, '2025-12-19 06:27:45.744764', '2025-12-19 06:28:13.337357');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('39c2337d-5275-88df-43f8-3397db46e9b0', 2, '2025-12-19 06:28:44.794274', '2025-12-19 06:30:51.239864');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('a681d469-b263-8b06-fda5-f7abe5126b97', 7, '2025-12-19 06:28:29.289708', '2025-12-19 06:30:53.230289');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c884f39a-2fd9-1df3-89b1-9f12ee6f8d8a', 7, '2025-12-19 06:33:31.591884', '2025-12-19 06:34:32.211');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b378675c-f079-9d80-48f5-924d10c7a072', 2, '2025-12-19 06:33:48.609042', '2025-12-19 06:34:33.463356');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d46ff041-33ac-0ae0-08dc-595658c32c79', 7, '2025-12-19 06:56:19.804333', '2025-12-19 06:56:41.046111');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f44289fb-f8ab-472e-f1ae-6a980570092b', 7, '2025-12-19 06:56:54.363355', '2025-12-19 06:58:08.852444');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8009d9b2-3f0c-d3a6-a2c8-8b67c19e6cf4', 7, '2025-12-19 06:59:57.913893', '2025-12-19 07:00:10.139833');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('7b9804f8-eefc-f951-412f-367c297ac30f', 7, '2025-12-19 07:07:02.95429', '2025-12-19 07:07:11.929518');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b67ca477-f88a-4e3d-d23b-bf948c3c34aa', 7, '2025-12-19 07:07:36.022479', '2025-12-19 07:08:38.275739');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3fe4e384-bc3e-7fdf-cce2-a46cf20ebef8', 7, '2025-12-19 07:08:48.184873', '2025-12-19 07:08:55.584451');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('26ffa713-2c4c-c8be-3dd7-c4e75ae55a03', 2, '2025-12-19 07:11:53.028541', '2025-12-19 07:12:02.900494');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('162b20ca-aabc-9f72-e971-fdbc6054c9a3', 7, '2025-12-19 07:11:27.371803', '2025-12-19 07:12:03.521128');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('734fb44e-c268-ecec-8ac5-ce88a9ff40b8', 7, '2025-12-19 07:15:45.513227', '2025-12-19 07:19:19.264264');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8f7aa421-6fc1-26d5-d249-c63b5eeeb8d6', 2, '2025-12-19 07:16:11.487371', '2025-12-19 07:19:20.521424');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c9baaa47-9944-f6d8-db85-32937e11f840', 2, '2025-12-19 07:21:46.702676', '2025-12-19 07:22:45.21858');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('52702a45-534a-bbc6-2ca6-181c16ae78f1', 7, '2025-12-19 07:21:26.416205', '2025-12-19 07:22:46.489678');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6ea2df1f-9260-1965-9cde-5300192a8906', 7, '2025-12-19 07:23:04.940316', '2025-12-19 07:23:58.526463');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('99e10dd9-225e-f6b8-3e13-b04441f9874d', 2, '2025-12-19 07:23:11.534216', '2025-12-19 07:26:56.501252');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b8884936-66e1-d686-f634-ae2caeefd324', 2, '2025-12-19 07:28:16.810226', '2025-12-19 07:31:49.626862');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('379b0c55-5acf-e861-782b-d5bb6aade5ba', 7, '2025-12-19 07:28:09.742704', '2025-12-19 07:31:52.493277');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('4d930d49-a94f-fbaf-b421-22a8850599e5', 7, '2025-12-19 07:34:33.132161', '2025-12-19 07:37:03.909864');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('534cd576-26e5-b882-8fb7-9188d3caddfd', 2, '2025-12-19 07:34:51.829693', '2025-12-19 07:37:05.588652');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0af9d363-2130-da70-1965-8c1e27c85220', 7, '2025-12-19 07:39:16.023995', '2025-12-19 07:39:42.949891');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8a25ef34-054c-a61b-ac0f-3d6441e9992a', 2, '2025-12-19 07:39:31.669056', '2025-12-19 07:39:43.428081');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f1946615-0ddd-4177-82c8-0d2c8e3e1ed0', 2, '2025-12-19 07:41:10.621513', '2025-12-19 07:42:01.901667');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('50b2bf5c-482b-778c-9d23-9bee55789f96', 7, '2025-12-19 07:41:44.094818', '2025-12-19 07:42:02.649326');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f6f2ebbe-88e5-5d4e-c12e-8dffd59736a2', 7, '2025-12-19 07:40:55.688404', '2025-12-19 07:42:02.649326');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('412edf0e-8c20-9bd4-eab9-a2f9415b3aad', 2, '2025-12-19 23:58:56.564175', '2025-12-20 00:01:28.804242');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0dcca433-fada-9687-35a6-ad7b1e6b7405', 7, '2025-12-19 23:57:52.223296', '2025-12-20 00:01:29.959861');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b50ceaef-f8f9-e3ae-bd90-135ecbe0e2d6', 7, '2025-12-20 00:18:36.407043', '2025-12-20 00:18:51.966692');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c0383962-525d-c694-71fe-3bb6f94423c6', 7, '2025-12-20 00:26:11.424687', '2025-12-20 00:26:39.486911');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('22aea55e-ba2b-857e-3b39-d6ac5294ecb0', 7, '2025-12-20 00:27:05.305576', '2025-12-20 00:27:15.498799');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('232bcf3d-ac50-bf39-81a8-93211cc04f5c', 7, '2025-12-20 00:28:10.401678', '2025-12-20 00:28:22.014329');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('fba63cea-9602-d0c7-e45b-8db210e3fddb', 7, '2025-12-20 00:41:39.414759', '2025-12-20 00:42:08.974814');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('744aa315-aaf8-993f-806e-96e9509d3b5e', 7, '2025-12-20 00:44:48.212825', '2025-12-20 00:50:01.639441');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('cdce1111-f5a9-6f01-72da-3a7d98bb1940', 2, '2025-12-20 00:52:26.69608', '2025-12-20 00:53:06.629367');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e166388b-b0e9-577c-9a80-d9e8b49d3454', 7, '2025-12-20 01:18:53.834279', '2025-12-20 01:19:37.807148');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('8996e3b0-d674-0fa8-a168-f0a031105f24', 7, '2025-12-20 01:20:56.281389', '2025-12-20 01:32:06.906636');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3d9c426c-f644-1dd4-3d7d-9eb4578db4cb', 4, '2025-12-20 01:22:35.532126', '2025-12-20 01:32:08.072238');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('0c77eb41-e3ee-4b3c-2cb3-030a1a384980', 7, '2025-12-20 01:57:35.543489', '2025-12-20 02:17:09.342371');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('20ddca4a-d89f-6f6e-57ef-a73fe602f52c', 2, '2025-12-20 01:57:53.307342', '2025-12-20 02:17:09.892124');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('40dcce84-15e7-d281-34a8-cf76d5143789', 2, '2025-12-20 03:06:32.703522', '2025-12-20 03:07:21.813929');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('f0ac264a-6525-7e97-2ad7-3908ca758d17', 7, '2025-12-20 03:06:01.886293', '2025-12-20 03:09:34.910904');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c021923b-3b8b-8a95-fd88-ba93a061a147', 4, '2025-12-20 03:07:36.603786', '2025-12-20 03:09:36.043107');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b42923f2-35e3-dab1-6151-97d1d876681b', 7, '2025-12-20 03:11:02.673243', '2025-12-20 03:12:07.020928');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6a993a34-6632-5d79-1f50-485902539edd', 4, '2025-12-20 03:11:26.532866', '2025-12-20 03:12:07.963531');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('83e12c21-e4b5-18e0-217c-27234866d766', 4, '2025-12-20 03:21:11.38342', '2025-12-20 03:22:11.329599');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('6b9c8a71-6bfd-0498-f9ed-a109744c8926', 7, '2025-12-20 03:21:20.21145', '2025-12-20 03:22:13.015193');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('91bfa2e4-2207-194c-2e9c-e46ab377403e', 7, '2025-12-20 04:58:20.954922', '2025-12-20 04:59:29.567741');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('90ba6402-fd96-c059-5c02-47b07c8cb3a5', 7, '2025-12-20 05:06:52.673635', '2025-12-20 05:08:11.354534');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('b72f4094-882b-9525-e0e4-56721d79126e', 7, '2025-12-20 05:06:37.375606', '2025-12-20 05:08:11.353035');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('52454a70-fb20-3eb8-01ea-66ed40dacde3', 2, '2025-12-20 05:07:08.759836', '2025-12-20 05:08:13.098912');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('20d3095e-38b0-11b4-48c5-db1b1d963bdd', 7, '2025-12-20 05:08:50.124932', '2025-12-20 05:10:12.10289');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('9d99af38-16ff-f849-5b6c-3b46c094abec', 4, '2025-12-20 05:09:15.039107', '2025-12-20 05:10:12.631026');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('9ee086a6-8b87-f41b-3fe8-ec006fb8e9b8', 4, '2025-12-20 05:57:47.510685', '2025-12-20 06:00:33.938351');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('98fdd3c1-bfe1-8e52-d5a0-d49f0e9c73c1', 7, '2025-12-20 05:58:46.256119', '2025-12-20 06:00:35.234106');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('c2f9c9d1-1eee-c91a-541e-3f1c436a4c3c', 7, '2025-12-20 05:54:51.209559', '2025-12-20 06:00:35.237094');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('d6094b1f-5c76-c273-a949-103e79c9e011', 7, '2025-12-20 06:13:15.266891', '2025-12-20 06:13:38.46499');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('7e5f2be0-12d7-3a16-2bca-ded1b2ce5384', 7, '2025-12-20 09:37:55.985206', '2025-12-20 09:38:06.419228');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('1f1ba68c-3796-104a-ddad-c6fb439593db', 2, '2025-12-20 09:38:24.17967', '2025-12-20 09:38:47.931306');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('59d7bfb2-0677-f500-213e-e6aa8dd9cd99', 2, '2025-12-20 09:48:03.390923', '2025-12-20 09:50:15.695823');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('9455d824-60fe-773e-0c62-4148a482e140', 2, '2025-12-20 09:50:32.919536', '2025-12-20 09:52:37.17267');
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('3f1aabe8-676b-0809-87f8-9664cc77c9ec', 2, '2025-12-20 09:56:38.557219', NULL);
INSERT INTO public.record_online_user (session_id, user_id, online_at, offline_at) VALUES ('e2862537-ce51-176a-f315-7ab2890f4a53', 4, '2025-12-20 09:58:15.861116', NULL);


ALTER TABLE public.record_online_user ENABLE TRIGGER ALL;

--
-- TOC entry 5210 (class 0 OID 17310)
-- Dependencies: 230
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
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (414, NULL, '2025-12-15 09:54:49.259894', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (415, 7, '2025-12-15 09:55:45.105238', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (416, 3, '2025-12-15 10:07:50.948664', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (417, 2, '2025-12-15 10:08:25.574149', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (418, 7, '2025-12-15 10:09:10.953234', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (419, 2, '2025-12-15 10:10:10.621931', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (420, 7, '2025-12-15 12:48:27.908857', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (421, 4, '2025-12-15 12:50:16.590312', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (422, 7, '2025-12-15 14:11:48.698163', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (423, 2, '2025-12-15 14:12:47.735107', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (424, 2, '2025-12-15 14:13:29.437979', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (425, 7, '2025-12-15 15:10:50.876928', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (426, 2, '2025-12-15 15:11:08.426142', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (427, 7, '2025-12-15 15:16:07.797213', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (428, 7, '2025-12-16 16:27:50.055174', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (429, 2, '2025-12-16 16:28:13.608077', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (430, 7, '2025-12-16 16:31:27.684845', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (431, 2, '2025-12-16 16:31:46.628923', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (432, 7, '2025-12-16 17:21:06.079163', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (433, 2, '2025-12-16 17:21:28.845549', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (434, 2, '2025-12-16 17:47:36.603702', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (435, 7, '2025-12-16 17:47:58.542198', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (436, 7, '2025-12-16 17:48:33.841982', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (437, 2, '2025-12-16 19:19:22.413016', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (438, 2, '2025-12-16 19:19:28.822782', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (439, 7, '2025-12-16 19:19:41.954924', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (440, 2, '2025-12-16 19:42:44.060453', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (441, 7, '2025-12-16 19:43:06.823786', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (442, 7, '2025-12-16 19:49:47.208698', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (443, 2, '2025-12-16 19:50:05.795567', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (444, 7, '2025-12-16 20:05:16.66348', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (445, 2, '2025-12-16 20:05:32.886205', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (446, 2, '2025-12-16 20:05:36.391711', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (447, 7, '2025-12-16 21:26:53.642318', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (448, 2, '2025-12-16 21:27:15.597862', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (449, 7, '2025-12-16 22:22:02.632182', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (450, 2, '2025-12-16 22:22:16.076552', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (451, 7, '2025-12-16 22:28:10.908164', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (452, 2, '2025-12-16 22:28:21.717889', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (453, 7, '2025-12-16 22:32:00.923086', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (454, 2, '2025-12-16 22:32:17.407218', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (455, 2, '2025-12-16 22:36:57.982473', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (456, 7, '2025-12-16 22:37:11.925323', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (457, 7, '2025-12-16 22:43:16.882398', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (458, 2, '2025-12-16 22:43:34.175866', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (459, 2, '2025-12-16 22:44:37.060844', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (460, 7, '2025-12-16 22:44:46.87476', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (461, 7, '2025-12-16 22:56:58.093826', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (462, 2, '2025-12-16 22:57:18.595584', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (463, 7, '2025-12-16 22:58:50.123496', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (464, 7, '2025-12-16 22:58:56.237221', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (465, 7, '2025-12-16 22:59:39.114125', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (466, 2, '2025-12-16 23:00:09.281145', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (467, 7, '2025-12-16 23:01:23.132917', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (468, 2, '2025-12-16 23:01:31.120585', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (469, 7, '2025-12-16 23:03:06.170232', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (470, 7, '2025-12-16 23:07:01.460955', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (471, 2, '2025-12-16 23:07:19.822054', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (472, 2, '2025-12-16 23:09:12.20532', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (473, 7, '2025-12-16 23:09:19.07705', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (474, 7, '2025-12-16 23:10:40.951508', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (475, 2, '2025-12-16 23:10:58.217155', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (476, 7, '2025-12-16 23:32:25.397997', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (477, 7, '2025-12-16 23:41:47.072278', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (478, 7, '2025-12-16 23:51:30.571089', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (479, 2, '2025-12-16 23:51:55.751', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (480, 7, '2025-12-17 00:11:58.011971', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (481, 2, '2025-12-17 00:12:15.976586', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (482, 7, '2025-12-17 00:15:53.552571', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (483, 7, '2025-12-17 00:20:10.430918', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (484, 7, '2025-12-17 00:21:39.453292', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (485, 7, '2025-12-17 00:22:51.350808', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (486, 2, '2025-12-17 00:23:05.595033', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (487, 2, '2025-12-17 00:24:26.866811', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (488, 7, '2025-12-17 00:24:39.822053', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (489, 7, '2025-12-17 00:28:53.883025', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (490, 7, '2025-12-17 00:29:42.988859', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (491, 7, '2025-12-17 00:32:27.900614', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (492, 2, '2025-12-17 00:32:45.147182', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (493, 4, '2025-12-17 00:34:33.652904', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (494, 7, '2025-12-17 00:35:17.632715', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (495, 7, '2025-12-17 00:36:51.991073', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (496, 7, '2025-12-17 01:18:00.01135', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (497, 2, '2025-12-17 01:18:28.148047', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (498, 2, '2025-12-17 01:26:35.809436', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (499, 2, '2025-12-17 01:34:42.034247', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (500, 2, '2025-12-17 01:41:31.49504', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (501, 7, '2025-12-17 01:42:02.989864', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (502, 2, '2025-12-17 01:42:36.260839', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (503, 2, '2025-12-17 13:48:08.534844', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (504, 7, '2025-12-17 13:48:16.236356', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (505, 7, '2025-12-17 13:48:36.218162', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (506, 7, '2025-12-17 14:05:01.363056', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (507, 7, '2025-12-17 14:06:36.284472', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (508, 7, '2025-12-17 14:09:51.081799', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (509, 7, '2025-12-17 14:11:17.289489', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (510, 7, '2025-12-17 14:13:47.227839', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (511, 7, '2025-12-17 14:14:33.56793', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (512, 7, '2025-12-17 14:57:12.997004', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (513, 7, '2025-12-17 15:21:13.738127', false);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (514, 7, '2025-12-17 15:21:20.946825', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (515, 7, '2025-12-17 15:47:20.599343', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (516, 7, '2025-12-17 15:50:24.983707', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (517, 7, '2025-12-17 15:52:27.217234', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (518, 7, '2025-12-17 15:53:35.568459', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (519, 7, '2025-12-17 15:53:54.095823', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (520, 2, '2025-12-17 15:54:50.106984', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (521, 7, '2025-12-17 15:57:45.583583', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (522, 2, '2025-12-17 15:57:55.387171', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (523, 7, '2025-12-17 16:02:03.856122', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (524, 2, '2025-12-17 16:02:12.337121', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (525, 7, '2025-12-17 16:08:09.086778', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (526, 7, '2025-12-17 16:17:06.19078', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (527, 2, '2025-12-17 16:17:48.550668', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (528, 7, '2025-12-17 16:25:29.860938', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (529, 7, '2025-12-17 16:29:19.676595', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (530, 2, '2025-12-17 16:30:46.450857', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (531, 7, '2025-12-17 16:33:57.754882', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (532, 2, '2025-12-17 16:34:15.294948', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (533, 7, '2025-12-19 02:33:36.716879', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (534, 2, '2025-12-19 02:34:29.622476', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (535, 7, '2025-12-19 02:38:00.778779', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (536, 2, '2025-12-19 02:38:25.140031', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (537, 7, '2025-12-19 02:53:25.524536', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (538, 2, '2025-12-19 02:54:12.713382', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (539, 4, '2025-12-19 02:57:08.275662', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (540, 7, '2025-12-19 04:02:18.208888', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (541, 7, '2025-12-19 04:03:11.323537', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (542, 2, '2025-12-19 04:03:32.571703', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (543, 7, '2025-12-19 04:07:10.128543', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (544, 7, '2025-12-19 04:15:03.54556', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (545, 7, '2025-12-19 04:15:09.261407', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (546, 7, '2025-12-19 04:37:07.869624', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (547, 7, '2025-12-19 04:39:22.923454', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (548, 7, '2025-12-19 04:41:23.270769', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (549, 7, '2025-12-19 04:42:33.107934', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (550, 7, '2025-12-19 04:56:12.158472', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (551, 7, '2025-12-19 05:01:13.064671', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (552, 7, '2025-12-19 05:04:26.257011', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (553, 2, '2025-12-19 05:05:09.452551', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (554, 7, '2025-12-19 05:32:43.749444', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (555, 2, '2025-12-19 05:33:06.302265', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (556, 7, '2025-12-19 05:45:56.675725', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (557, 2, '2025-12-19 05:46:24.364425', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (558, 7, '2025-12-19 05:52:48.469902', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (559, 2, '2025-12-19 05:53:03.95173', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (560, 7, '2025-12-19 05:57:07.579444', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (561, 7, '2025-12-19 05:57:44.482284', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (562, 2, '2025-12-19 05:58:09.850339', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (563, 7, '2025-12-19 06:01:25.959945', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (564, 2, '2025-12-19 06:01:47.203286', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (565, 7, '2025-12-19 06:11:15.305476', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (566, 2, '2025-12-19 06:11:52.784641', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (567, 7, '2025-12-19 06:17:56.09006', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (568, 2, '2025-12-19 06:18:11.637224', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (569, 7, '2025-12-19 06:27:05.752183', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (570, 2, '2025-12-19 06:27:16.804141', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (571, 4, '2025-12-19 06:27:45.150304', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (572, 7, '2025-12-19 06:28:28.758909', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (573, 2, '2025-12-19 06:28:44.189699', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (574, 7, '2025-12-19 06:33:31.033001', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (575, 2, '2025-12-19 06:33:48.005064', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (576, 7, '2025-12-19 06:56:19.25833', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (577, 7, '2025-12-19 06:56:53.860398', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (578, 7, '2025-12-19 06:59:57.376048', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (579, 7, '2025-12-19 07:07:02.420163', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (580, 7, '2025-12-19 07:07:35.415106', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (581, 7, '2025-12-19 07:08:47.64289', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (582, 7, '2025-12-19 07:11:26.800686', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (583, 2, '2025-12-19 07:11:52.415749', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (584, 7, '2025-12-19 07:15:44.976645', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (585, 2, '2025-12-19 07:16:10.932701', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (586, 7, '2025-12-19 07:21:25.822715', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (587, 2, '2025-12-19 07:21:46.132941', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (588, 7, '2025-12-19 07:23:04.38852', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (589, 2, '2025-12-19 07:23:10.931395', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (590, 7, '2025-12-19 07:28:09.211279', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (591, 2, '2025-12-19 07:28:16.216268', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (592, 7, '2025-12-19 07:34:32.566003', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (593, 2, '2025-12-19 07:34:51.167835', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (594, 7, '2025-12-19 07:39:15.485865', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (595, 2, '2025-12-19 07:39:31.079867', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (596, 7, '2025-12-19 07:40:55.167756', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (597, 2, '2025-12-19 07:41:10.026361', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (598, 7, '2025-12-19 07:41:43.913067', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (599, 7, '2025-12-19 23:57:51.142525', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (600, 2, '2025-12-19 23:58:55.955876', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (601, 7, '2025-12-20 00:18:35.812899', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (602, 7, '2025-12-20 00:26:10.885858', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (603, 7, '2025-12-20 00:27:04.756164', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (604, 7, '2025-12-20 00:28:09.858533', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (605, 7, '2025-12-20 00:41:38.842745', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (606, 7, '2025-12-20 00:44:47.661183', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (607, 2, '2025-12-20 00:52:26.1326', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (608, 7, '2025-12-20 01:18:53.268237', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (609, 7, '2025-12-20 01:20:55.70081', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (610, 4, '2025-12-20 01:22:34.96081', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (611, 7, '2025-12-20 01:57:34.523574', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (612, 2, '2025-12-20 01:57:52.713208', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (613, 7, '2025-12-20 03:06:00.872374', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (614, 2, '2025-12-20 03:06:32.110286', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (615, 4, '2025-12-20 03:07:36.047776', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (616, 7, '2025-12-20 03:11:02.153319', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (617, 4, '2025-12-20 03:11:25.953629', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (618, 4, '2025-12-20 03:21:10.367226', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (619, 7, '2025-12-20 03:21:19.663247', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (620, 7, '2025-12-20 04:58:19.965594', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (621, 7, '2025-12-20 05:06:36.424627', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (622, 7, '2025-12-20 05:06:52.483018', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (623, 2, '2025-12-20 05:07:08.169084', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (624, 7, '2025-12-20 05:08:49.58271', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (625, 4, '2025-12-20 05:09:14.46243', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (626, 7, '2025-12-20 05:54:50.226032', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (627, 4, '2025-12-20 05:57:46.955537', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (628, 7, '2025-12-20 05:58:46.058028', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (629, 7, '2025-12-20 06:13:14.68255', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (630, 7, '2025-12-20 09:37:54.945178', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (631, 2, '2025-12-20 09:38:23.568696', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (632, 2, '2025-12-20 09:48:02.822112', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (633, 2, '2025-12-20 09:50:32.396845', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (634, 2, '2025-12-20 09:56:37.559836', true);
INSERT INTO public.record_signin (record_signin_id, user_id, signed_in_at, is_successful) VALUES (635, 4, '2025-12-20 09:58:15.656407', true);


ALTER TABLE public.record_signin ENABLE TRIGGER ALL;

--
-- TOC entry 5211 (class 0 OID 17323)
-- Dependencies: 231
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.report DISABLE TRIGGER ALL;

INSERT INTO public.report (reporter_id, reported_user_id, reason, reported_at, status) VALUES (2, 4, 'Ngôn từ khiếm nhã', '2025-12-13 12:23:41.286185', 0);
INSERT INTO public.report (reporter_id, reported_user_id, reason, reported_at, status) VALUES (2, 5, 'Scammer', '2025-12-13 23:01:29.196856', 0);


ALTER TABLE public.report ENABLE TRIGGER ALL;

--
-- TOC entry 5206 (class 0 OID 17269)
-- Dependencies: 226
-- Data for Name: request_password_reset; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.request_password_reset DISABLE TRIGGER ALL;



ALTER TABLE public.request_password_reset ENABLE TRIGGER ALL;

--
-- TOC entry 5204 (class 0 OID 17249)
-- Dependencies: 224
-- Data for Name: verify_email_change_token; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.verify_email_change_token DISABLE TRIGGER ALL;



ALTER TABLE public.verify_email_change_token ENABLE TRIGGER ALL;

--
-- TOC entry 5202 (class 0 OID 17230)
-- Dependencies: 222
-- Data for Name: verify_token; Type: TABLE DATA; Schema: public; Owner: -
--

ALTER TABLE public.verify_token DISABLE TRIGGER ALL;



ALTER TABLE public.verify_token ENABLE TRIGGER ALL;

--
-- TOC entry 5237 (class 0 OID 0)
-- Dependencies: 241
-- Name: group_conversation_group_conversation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.group_conversation_group_conversation_id_seq', 22, true);


--
-- TOC entry 5238 (class 0 OID 0)
-- Dependencies: 245
-- Name: group_conversation_message_group_conversation_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.group_conversation_message_group_conversation_message_id_seq', 121, true);


--
-- TOC entry 5239 (class 0 OID 0)
-- Dependencies: 238
-- Name: private_conversation_message_private_conversation_message_i_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.private_conversation_message_private_conversation_message_i_seq', 39, true);


--
-- TOC entry 5240 (class 0 OID 0)
-- Dependencies: 235
-- Name: private_conversation_private_conversation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.private_conversation_private_conversation_id_seq', 21, true);


--
-- TOC entry 5241 (class 0 OID 0)
-- Dependencies: 229
-- Name: record_signin_record_signin_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.record_signin_record_signin_id_seq', 635, true);


--
-- TOC entry 5242 (class 0 OID 0)
-- Dependencies: 225
-- Name: request_password_reset_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.request_password_reset_request_id_seq', 1, false);


--
-- TOC entry 5243 (class 0 OID 0)
-- Dependencies: 249
-- Name: request_password_reset_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5244 (class 0 OID 0)
-- Dependencies: 219
-- Name: user_info_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_info_user_id_seq', 7, true);


--
-- TOC entry 5245 (class 0 OID 0)
-- Dependencies: 250
-- Name: verify_email_change_token_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5246 (class 0 OID 0)
-- Dependencies: 223
-- Name: verify_email_change_token_verification_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.verify_email_change_token_verification_id_seq', 1, false);


--
-- TOC entry 5247 (class 0 OID 0)
-- Dependencies: 251
-- Name: verify_token_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--



--
-- TOC entry 5248 (class 0 OID 0)
-- Dependencies: 221
-- Name: verify_token_verification_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.verify_token_verification_id_seq', 1, false);


-- Completed on 2025-12-20 10:02:37

--
-- PostgreSQL database dump complete
--

