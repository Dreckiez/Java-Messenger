package utils;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.WebSocketHttpHeaders;

import models.NotificationWsResponse;
import services.NotificationSocketListener;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.converter.JacksonJsonMessageConverter;

public class SocketManager {

    private static StompSession session;

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
                        // subscribeChat();
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

    // private static void subscribeChat() {
    // session.subscribe("/user/client/chat",
    // new GenericFrameHandler<>(ChatMessage.class,
    // ChatSocketListener::onMessage));
    // }
}
