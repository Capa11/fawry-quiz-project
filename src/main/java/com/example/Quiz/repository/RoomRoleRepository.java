package com.example.Quiz.repository;

import com.example.Quiz.model.RoomRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRoleRepository extends JpaRepository<RoomRole, RoomRole.RoomRoleId> {
    Page<RoomRole> findByIdRoomId(Long roomId, Pageable pageable);
    Boolean existsById_RoomIdAndId_RoleNameIgnoreCase(Long idRoomId, String idRoleName);

    @Query(nativeQuery = true, value = """
                select rr.* from user_room_role urr
                join room_roles rr on rr.room_id = urr.room_id and urr.role_name=rr.role_name
                where rr.room_id = (select a.room_id from assignments a where a.id = :assignmentId)
                and urr.user_id = :userId
            """)
    Optional<RoomRole> findByUserIdAndAssignmentId(Long userId, Long assignmentId);
}
