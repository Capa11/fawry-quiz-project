package com.example.Quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "room_roles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RoomRole {

    @EmbeddedId
    @Builder.Default

    private RoomRoleId id = new RoomRoleId();


    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    @Column(name = "can_add_user", nullable = false)
    @Builder.Default
    private Boolean canAddUser=false;

    @Column(name = "can_add_role", nullable = false)
    @Builder.Default
    private Boolean canAddRole=false;

    @Column(name = "can_add_assignment", nullable = false)
    @Builder.Default
    private Boolean canAddAssignment=false;

    @Column(name = "can_post", nullable = false)
    @Builder.Default
    private Boolean canPost=false;

    @Column(name = "can_answer_question", nullable = false)
    @Builder.Default
    private Boolean canAnswerQuestion=false;

    @Column(name = "can_mark_question", nullable = false)
    @Builder.Default
    private Boolean canMarkQuestion=false;

    @Column(name = "can_see_user_details", nullable = false)
    @Builder.Default
    private Boolean canSeeUserDetails=false;

    @Column(name = "can_update_room", nullable = false)
    @Builder.Default
    private Boolean canUpdateRoom=false;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "roomRole", cascade = CascadeType.ALL)
    private List<UserRoomRole> userRoomRoles;

    public String getRoleName() {
        return id.getRoleName();
    }

    public void setRoleName(String roleName) {
        id.setRoleName(roleName);
    }

    public RoomRole(Room room, String roleName,
                    Boolean canAddUser,
                    Boolean canAddRole,
                    Boolean canAddAssignment,
                    Boolean canPost,
                    Boolean canAnswerQuestion,
                    Boolean canMarkQuestion,
                    Boolean canSeeUserDetails,
                    Boolean canUpdateRoom) {
        this.room = room;
        this.id.setRoleName(roleName);
        this.canAddUser = canAddUser;
        this.canAddRole = canAddRole;
        this.canAddAssignment = canAddAssignment;
        this.canPost = canPost;
        this.canAnswerQuestion = canAnswerQuestion;
        this.canMarkQuestion = canMarkQuestion;
        this.canSeeUserDetails = canSeeUserDetails;
        this.canUpdateRoom = canUpdateRoom;
    }
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @Getter
    @Embeddable
    public static class RoomRoleId implements Serializable {
        @Column(name = "room_id")
        private Long roomId;

        @Size(min = 1, max = 100)
        @Column(name = "role_name", length = 100)
        private String roleName;

        public RoomRoleId() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RoomRoleId that)) return false;
            return Objects.equals(roomId, that.roomId) &&
                    Objects.equals(roleName, that.roleName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roomId, roleName);
        }
        public RoomRoleId(Long roomId, String roleName) {
            this.roomId = roomId;
            this.roleName = roleName;
        }
    }

}
