package com.example.Quiz.controller;

import com.example.Quiz.model.Room;
import com.example.Quiz.payload.RoomDTO;
import com.example.Quiz.service.RoomService;
import com.example.Quiz.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class UserRoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<Page<Room>> getUserRooms(
            @AuthenticationPrincipal AuthUser authUser,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(roomService.getUserRooms(authUser.getUser(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(roomService.getRoomById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDTO roomDTO, @AuthenticationPrincipal AuthUser authUser) {
        roomService.createRoom(roomDTO, authUser);
        return ResponseEntity.ok("Room created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
    @PostMapping("{id}/add-student/{userName}")
    public ResponseEntity<?> addUser(@PathVariable Long id,@PathVariable String userName) {
        roomService.addUser(userName,id);
        return ResponseEntity.ok("user added successfully");
    }
    //NOTE: HANDLE JOINBYLINK
}
