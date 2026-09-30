package com.example.Quiz.controller;

import com.example.Quiz.model.RoomRole;
import com.example.Quiz.service.RoomRoleService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms/{roomId}/roles")
public class RoomRoleController {

    private final RoomRoleService roomRoleService;

    @GetMapping
    public ResponseEntity<Page<RoomRole>> getRoomRoles(@PathVariable Long roomId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(roomRoleService.getRoomRoles(roomId, pageable));
    }


    @PostMapping
    public ResponseEntity<RoomRole> createRoomRole(@PathVariable Long roomId, @RequestBody RoomRole role) {
        return ResponseEntity.ok(roomRoleService.createRoomRole(roomId, role));
    }

    @PutMapping("/{roleName}")
    public ResponseEntity<RoomRole> updateRoomRole(@PathVariable Long roomId,
                                                   @PathVariable String roleName,
                                                   @RequestBody RoomRole updated) {
        return ResponseEntity.ok(roomRoleService.updateRoomRole(roomId, roleName, updated));
    }

    @DeleteMapping("/{roleName}")
    public ResponseEntity<?> deleteRoomRole(@PathVariable Long roomId, @PathVariable String roleName) {
        roomRoleService.deleteRoomRole(roomId, roleName);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{roleName}")
    public ResponseEntity<Boolean> checkRoleExists(@PathVariable Long roomId, @PathVariable String roleName) {
        return  ResponseEntity.ok(roomRoleService.checkRoleExists(roomId, roleName));
    }
}
