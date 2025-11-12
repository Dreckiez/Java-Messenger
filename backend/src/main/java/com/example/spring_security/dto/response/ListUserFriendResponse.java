package com.example.spring_security.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListUserFriendResponse {
    List<UserFriendResponse> listOfFriend;
    Long count;
}
