package com.example.Quiz.service;

import com.example.Quiz.model.Room;
import com.example.Quiz.model.RoomRole;
import com.example.Quiz.model.User;
import com.example.Quiz.model.UserRoomRole;
import com.example.Quiz.model.enums.Roles;
import com.example.Quiz.payload.RoomDTO;
import com.example.Quiz.repository.RoomRepository;
import com.example.Quiz.repository.RoomRoleRepository;
import com.example.Quiz.repository.UserRepository;
import com.example.Quiz.repository.UserRoomRoleRepository;
import com.example.Quiz.security.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomRoleRepository userRoomRoleRepository;
    private final RoomRoleRepository roomRoleRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Room> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Room> getUserRooms(User currentUser, Pageable pageable) {
        return roomRepository.findByUserId(currentUser.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id " + id));
    }

    @Transactional
    public void createRoom(RoomDTO roomDTO, AuthUser authUser) throws DataIntegrityViolationException {
        Room room = new Room(roomDTO.getName(),roomDTO.getJoinByLink(),roomDTO.getIsQuestionAllowed(),roomDTO.getIsCommentAllowed());
        roomRepository.save(room);
        RoomRole owner = new RoomRole();
        owner.setRoom(room);
        owner.setRoleName("OWNER");
        owner.setId(new RoomRole.RoomRoleId(room.getId(),"OWNER"));
        owner.setCanAddUser(true);
        owner.setCanPost(true);
        owner.setCanAddAssignment(true);
        owner.setCanMarkQuestion(true);
        owner.setCanSeeUserDetails(true);
        owner.setCanAnswerQuestion(true);
        owner.setCanAddRole(true);

        RoomRole admin = new RoomRole();
        admin.setRoom(room);
        admin.setRoleName("ADMIN");
        admin.setCanAddUser(true);
        admin.setCanPost(true);
        admin.setCanAddAssignment(true);
        admin.setCanMarkQuestion(true);
        admin.setCanSeeUserDetails(true);
        admin.setCanAnswerQuestion(true);
        admin.setCanAddRole(true);

        RoomRole student = new RoomRole();
        admin.setRoom(room);
        admin.setRoleName("STUDENT");
        admin.setCanAddUser(false);
        admin.setCanPost(false);
        admin.setCanAddAssignment(false);
        admin.setCanMarkQuestion(false);
        admin.setCanSeeUserDetails(false);
        admin.setCanAnswerQuestion(false);
        admin.setCanAddRole(false);

        roomRoleRepository.saveAll(List.of(owner,admin,student));
        UserRoomRole userRoomRole = new UserRoomRole(authUser.getUser(),owner);
        userRoomRoleRepository.save(userRoomRole);
    }


    @Transactional
    public void updateRoom(Long roomId, RoomDTO roomDTO) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (user.getRole() == Roles.ROLE_ADMIN||getRoomRole(user,roomId).getCanUpdateRoom()) {
            Room room = getRoomById(roomId);

            room.setName(roomDTO.getName());
            room.setJoinByLink(roomDTO.getJoinByLink());
            room.setIsQuestionAllowed(roomDTO.getIsQuestionAllowed());
            room.setIsCommentAllowed(roomDTO.getIsCommentAllowed());

            roomRepository.save(room);
        }
    }

    @Transactional
    public void verifyRoom(Long roomId) {
        Room room = getRoomById(roomId);
        room.setIsVerified(true);
        roomRepository.save(room);
    }


    @Transactional
    public void deleteRoom(Long roomId) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        UserRoomRole userRoomRole = userRoomRoleRepository.findByUserIdAndRoomId(user.getId(),roomId).get();
        RoomRole roomRole = userRoomRole.getRoomRole();
        if(roomRole.getRoleName().equals("OWNER")){
            roomRepository.deleteById(roomId);
        }
        else{
            throw new DataIntegrityViolationException("You do not have permission to update this room");
        }
    }

    @Transactional(readOnly = true)
    public RoomRole getRoomRole(User user, Long roomId) {
        return userRoomRoleRepository.findByUserIdAndRoomId(user.getId(), roomId).orElseThrow().getRoomRole();
    }
    @Transactional
    public void addUser(String userName, Long roomId) {
        User user = ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if(user.getRole() == Roles.ROLE_ADMIN||getRoomRole(user,roomId).getCanAddUser()) {
            UserRoomRole userRoomRole = UserRoomRole.builder()
                    .user(userRepository.findByUsername(userName).orElseThrow())
                    .roomRole(roomRoleRepository.findById(new RoomRole.RoomRoleId(roomId,"STUDENT")).orElseThrow())
                    .build();
            userRoomRoleRepository.save(userRoomRole);
        }
    }

}
