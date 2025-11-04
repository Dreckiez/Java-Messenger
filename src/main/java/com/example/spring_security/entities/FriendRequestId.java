package com.example.spring_security.entities.Enum;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestId implements Serializable {
    private Long senderId;
    private Long receiverId;
    private LocalDateTime sentAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequestId that)) return false;
        return Objects.equals(senderId, that.senderId)
                && Objects.equals(receiverId, that.receiverId)
                && Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId, sentAt);
    }
}