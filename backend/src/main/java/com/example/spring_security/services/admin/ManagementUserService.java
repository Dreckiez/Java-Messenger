package com.example.spring_security.services.admin;

import com.example.spring_security.dto.request.ManageUserRequest;
import com.example.spring_security.dto.response.ListReportResponse;
import com.example.spring_security.dto.response.ListUserFriendResponse;
import com.example.spring_security.dto.response.ResetPasswordResponse;
import com.example.spring_security.entities.RecordSignIn;
import com.example.spring_security.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ManagementUserService {
    List<User> getUserDetailList(String keyword, Boolean isActive,
                                 Boolean isAccepted, Integer greaterThan,
                                 Integer smallerThan, String sort,
                                 Integer days);

    Map<String, String> createUser(ManageUserRequest createUserRequest);

    Map<String, String> deleteUser(Long userId);

    Map<String, String> updateUser(ManageUserRequest updateUserRequest, Long userId);

    ResetPasswordResponse resetPassword(Long userId);

    List<RecordSignIn> getRecordSignIn(Boolean isSuccessful, boolean sort, Long userId);

    ListUserFriendResponse getFriends(Long userId, String keyword, String sortBy);

    ListReportResponse getReports(String sortBy, String username, String email, LocalDate startDate, LocalDate endDate);
}
