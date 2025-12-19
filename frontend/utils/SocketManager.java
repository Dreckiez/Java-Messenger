package utils;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.WebSocketHttpHeaders;

import models.MessageWsResponse;
import models.NotificationWsResponse;
import services.MessageSocketListener;
import services.NotificationSocketListener;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;

public class SocketManager {

    private static StompSession session;

    private static Map<Long, Subscription> groupSubscriptions = new ConcurrentHashMap<>();

    private static Set<Long> pendingGroupIds = ConcurrentHashMap.newKeySet();

    public static void connect() {

        if (UserSession.getUser() == null) {
            System.err.println("DEBUG: Cannot connect to WebSocket. No user logged in.");
            return;
        }

        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());

        client.setMessageConverter(new JacksonJsonMessageConverter());

        StompHeaders connectHeaders = new StompHeaders();
        String token = UserSession.getUser().getToken(); // Get the logged-in token
        connectHeaders.add("Authorization", "Bearer " + token);

        System.out.println("DEBUG: Connecting to WebSocket...");

        CompletableFuture<StompSession> futureSession = client.connectAsync("ws://localhost:8080/ws",
                new WebSocketHttpHeaders(),
                connectHeaders,
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession s, StompHeaders h) {
                        System.out.println("DEBUG: WebSocket Connected! Session ID: " + s.getSessionId());
                        session = s;
                        subscribeNotifications();
                        subscribeChat();

                        if (!pendingGroupIds.isEmpty()) {
                            System.out.println(
                                    "DEBUG: Processing " + pendingGroupIds.size() + " pending group subscriptions...");
                            for (Long groupId : pendingGroupIds) {
                                performSubscription(groupId);
                            }
                            pendingGroupIds.clear();
                        }
                    }

                    @Override
                    public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable t) {
                        System.err.println("DEBUG: WebSocket Exception: " + t.getMessage());
                        t.printStackTrace();
                    }

                    @Override
                    public void handleTransportError(StompSession s, Throwable t) {
                        System.err.println("DEBUG: Transport Error: " + t.getMessage());
                    }
                });
    }

    private static void subscribeNotifications() {
        String destination = "/user/client/notifications";

        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return NotificationWsResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("DEBUG: WebSocket Notification Received!");
                NotificationSocketListener.onMessage((NotificationWsResponse) payload);
            }
        });
        System.out.println("DEBUG: Subscribed to " + destination);

    }

    private static void subscribeChat() {
        String destination = "/user/client/messages";
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return java.util.Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    // 1. Convert payload to JSONObject
                    JSONObject json = new JSONObject((java.util.Map) payload);

                    // 2. Parse into our Model
                    MessageWsResponse msg = MessageWsResponse.fromJson(json);

                    // 3. Pass to Listener
                    MessageSocketListener.onMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("DEBUG: Subscribed to " + destination);
    }

    public static void subscribeGroup(long groupId) {
        if (session == null || !session.isConnected()) {
            // 🔥 Session not ready? Add to queue!
            System.out.println("⏳ Session not ready. Queuing Group " + groupId);
            pendingGroupIds.add(groupId);
            return;
        }

        performSubscription(groupId);
    }

    private static void performSubscription(long groupId) {
        // Prevent duplicates
        if (groupSubscriptions.containsKey(groupId))
            return;

        // Matches your Backend: /client/group/{id}
        String destination = "/client/group/" + groupId;

        System.out.println("DEBUG: Subscribing to " + destination);

        Subscription sub = session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return java.util.Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    JSONObject json = new JSONObject((java.util.Map) payload);
                    MessageWsResponse msg = MessageWsResponse.fromJson(json);
                    MessageSocketListener.onMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        groupSubscriptions.put(groupId, sub);
    }

    public static void unsubscribeGroup(long groupId) {
        if (groupSubscriptions.containsKey(groupId)) {
            groupSubscriptions.get(groupId).unsubscribe();
            groupSubscriptions.remove(groupId);
            System.out.println("DEBUG: Unsubscribed from Group " + groupId);
        }
        pendingGroupIds.remove(groupId);
    }
}
