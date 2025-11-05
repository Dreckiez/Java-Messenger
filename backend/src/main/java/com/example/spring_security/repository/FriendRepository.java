package com.example.spring_security.repository;

import com.example.spring_security.dto.response.UserFriendResponse;
import com.example.spring_security.entities.Friend;
import com.example.spring_security.entities.FriendId;
import com.example.spring_security.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {


    List<Friend> findByIdUserId1OrIdUserId2(Long userId1, Long userId2);


    @Query(value = """
    SELECT * FROM (
        SELECT u.*
        FROM friend f
        JOIN "user_info" u ON u.user_id = f.user_id2
        WHERE f.user_id1 = :userId
          AND ( :keyword = '' OR
                LOWER(CONCAT(u.last_name, ' ', u.first_name)) LIKE LOWER(CONCAT('%', :keyword, '%')
              ))

        UNION ALL

        SELECT u.*
        FROM friend f
        JOIN "user_info" u ON u.user_id = f.user_id1
        WHERE f.user_id2 = :userId
          AND ( :keyword = '' OR
                LOWER(CONCAT(u.last_name, ' ', u.first_name)) LIKE LOWER(CONCAT('%', :keyword, '%')
                ))
    ) AS all_friends
    ORDER BY all_friends.is_online DESC
    """, nativeQuery = true)
    List<User> findFriendsByUserIdAndKeywordOrderByOnline(
            @Param("userId") Long userId,
            @Param("keyword") String keyword);




    @Query(value = """
    SELECT *
    FROM friend fr
    WHERE (fr.user_id1 = :userId1 AND fr.user_id2 = :userId2)
       OR (fr.user_id1 = :userId2 AND fr.user_id2 = :userId1)
    """, nativeQuery = true)
    Optional<Friend> findExistingFriendBetween(@Param("userId1") Long userId1,
                                               @Param("userId2") Long userId2);

}
