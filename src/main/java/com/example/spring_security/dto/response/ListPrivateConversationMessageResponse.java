package com.example.spring_security.dto.response;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ListPrivateConversationMessageResponse extends BaseUserResponse {

    Boolean isOnline;

    Long privateConversationId;

    List<PrivateConversationMessageResponse> privateConversationMessageResponseList;
}
