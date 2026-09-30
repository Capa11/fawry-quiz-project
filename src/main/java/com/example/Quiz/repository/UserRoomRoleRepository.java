package com.example.Quiz.repository;

import com.example.Quiz.model.RoomRole;
import com.example.Quiz.model.User;
import com.example.Quiz.model.UserRoomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoomRoleRepository extends JpaRepository<UserRoomRole, Long> {
    @Query(
            value = "SELECT urr  FROM UserRoomRole urr " +
                    "WHERE urr.user.id = :userId AND urr.roomId = :roomId"
    )
    Optional<UserRoomRole> findByUserIdAndRoomId(
            @Param("userId") Long userId,
            @Param("roomId") Long roomId
    );
}
