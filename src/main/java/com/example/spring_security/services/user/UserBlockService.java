package com.example.spring_security.services;


import com.example.spring_security.dto.request.RemoveBlockRequest;
import com.example.spring_security.dto.response.UserBlockResponse;
import com.example.spring_security.entities.Block;
import com.example.spring_security.entities.User;

import java.util.List;
import java.util.Map;

public interface UserBlockService {
    Map<String, String> blockRequest(Long blockerId, Long blockedUserId);
    List<UserBlockResponse> getBlockList(Long blockerId);
    Map<String, String> removeRequest(RemoveBlockRequest removeBlockRequest);
}
