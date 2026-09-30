package com.example.Quiz.service;

import com.example.Quiz.model.Room;
import com.example.Quiz.model.RoomRole;
import com.example.Quiz.model.RoomRole.RoomRoleId;
import com.example.Quiz.model.User;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.repository.RoomRepository;
import com.example.Quiz.repository.RoomRoleRepository;
import com.example.Quiz.repository.UserRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomRoleService {

    private final RoomRoleRepository roomRoleRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;

    @Transactional(readOnly = true)
    public Page<RoomRole> getRoomRoles(Long roomId, Pageable pageable) {
        if(checkAuthorization(roomId)) return roomRoleRepository.findByIdRoomId(roomId, pageable);
        else throw new DataIntegrityViolationException("You do not have permission to get those roles");
    }

    @Transactional(readOnly = true)
    public RoomRole getRole(Long roomId, String roleName) {
        if(checkAuthorization(roomId)) {
            RoomRoleId id = new RoomRoleId(roomId, roleName);
            return roomRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        }
        else throw new DataIntegrityViolationException("You do not have permission to get this role");
    }

    @Transactional
    public RoomRole createRoomRole(Long roomId, RoomRole role) {
        if(checkAuthorization(roomId)) {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
            role.setRoom(room);
            role.getId().setRoomId(roomId);
            return roomRoleRepository.save(role);
        }
        else throw new DataIntegrityViolationException("You do not have permission to create this role");
    }

    @Transactional
    public RoomRole updateRoomRole(Long roomId, String roleName, RoomRole updated) {
        if(checkAuthorization(roomId)) {
            RoomRole existing = getRole(roomId, roleName);
            existing.setCanAddUser(updated.getCanAddUser());
            existing.setCanAddRole(updated.getCanAddRole());
            existing.setCanAddAssignment(updated.getCanAddAssignment());
            existing.setCanPost(updated.getCanPost());
            existing.setCanAnswerQuestion(updated.getCanAnswerQuestion());
            existing.setCanMarkQuestion(updated.getCanMarkQuestion());
            existing.setCanSeeUserDetails(updated.getCanSeeUserDetails());
            existing.setCanUpdateRoom(updated.getCanUpdateRoom());
            return roomRoleRepository.save(existing);
        }
        else throw new DataIntegrityViolationException("You do not have permission to update this role");
    }

    @Transactional
    public void deleteRoomRole(Long roomId, String roleName) {
        if(roleName.equals("STUDENT")||roleName.equals("OWNER")||roleName.equals("ADMIN"))throw new DataIntegrityViolationException("This role cannot be deleted");
        if(checkAuthorization(roomId)) {
            RoomRoleId id = new RoomRoleId(roomId, roleName);
            roomRoleRepository.deleteById(id);
        }
        else throw new DataIntegrityViolationException("You do not have permission to delete this role");
    }
    @Transactional(readOnly = true)
    public Boolean checkRoleExists(Long roomId, String roleName) {
        if(checkAuthorization(roomId)) {
            return roomRoleRepository.existsById_RoomIdAndId_RoleNameIgnoreCase(roomId,roleName);
        }
        else throw new DataIntegrityViolationException("You do not have permission to check this role");
    }
    @Transactional(readOnly = true)
    public Boolean checkAuthorization(Long roomId) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return user.getRole().equals(Roles.ROLE_ADMIN) || userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId).orElseThrow().getRoomRole().getCanAddRole();
    }
}
