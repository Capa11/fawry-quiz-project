package com.example.Quiz.repository;

import com.example.Quiz.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("""
            SELECT r FROM Room r
            WHERE EXISTS (
                SELECT urr.id FROM UserRoomRole urr
                WHERE urr.roomId = r.id AND urr.user.id = :userId
            )
            """)
    Page<Room> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
