package com.example.Quiz.controller;

import com.example.Quiz.model.Room;
import com.example.Quiz.service.RoomService;
import com.example.Quiz.payload.RoomDTO;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    private final RoomService roomService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Page<Room> getAllRooms(@ParameterObject Pageable pageable) {
        return roomService.getAllRooms(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody RoomDTO roomDTO) {
        roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok("Room updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
