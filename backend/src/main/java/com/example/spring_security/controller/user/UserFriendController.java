package com.example.spring_security.controller;

import com.example.spring_security.dto.request.UpdateStatusFriendRequest;
import com.example.spring_security.dto.response.FriendRequestResponse;
import com.example.spring_security.dto.response.UserFriendResponse;
import com.example.spring_security.entities.FriendRequest;
import com.example.spring_security.entities.User;
import com.example.spring_security.services.UserFriendService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/chat/user/friend")
@RequiredArgsConstructor
public class UserFriendController {
    private final UserFriendService userFriendService;


    @GetMapping("/list")
    public ResponseEntity<List<UserFriendResponse>> getFriendList(@AuthenticationPrincipal User user, @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        return ResponseEntity.ok(userFriendService.getFriendList(user.getUserId(), keyword));
    }

    @PostMapping("/request/{id}")
    public ResponseEntity<Map<String, String>> friendRequest(@AuthenticationPrincipal User sender, @PathVariable("id") Long receiverId) {
        return ResponseEntity.ok(userFriendService.friendRequest(sender.getUserId(), receiverId));
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<Map<String, String>> removeFriend(@AuthenticationPrincipal User remover, @PathVariable("id") Long removedUserId) {
        return ResponseEntity.ok(userFriendService.removeFriend(remover.getUserId(), removedUserId));
    }

    @GetMapping("/list-request-received")
    public ResponseEntity<List<FriendRequestResponse>> getListFriendRequestReceived
            (@AuthenticationPrincipal User user,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        return ResponseEntity.ok(userFriendService.getListFriendRequestReceive(user.getUserId(), keyword));
    }
    @GetMapping("/list-request-sent")
    public ResponseEntity<List<FriendRequestResponse>> getListFriendRequestSent
            (@AuthenticationPrincipal User user,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        return ResponseEntity.ok(userFriendService.getListFriendRequestSent(user.getUserId(), keyword));
    }
    @PostMapping("/update-status")
    public ResponseEntity<Map<String, String>> updateStatus(@RequestBody UpdateStatusFriendRequest updateStatusFriendRequest,
                                                            @AuthenticationPrincipal User updator) {
        return ResponseEntity.ok(userFriendService.updateStatus(updateStatusFriendRequest, updator.getUserId()));
    }
}
