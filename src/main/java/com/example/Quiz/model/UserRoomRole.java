package com.example.Quiz.model;

import com.example.Quiz.model.Room;
import com.example.Quiz.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(
        name = "user_room_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "room_id"}),
        }
)@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
public class UserRoomRole {

    public UserRoomRole(User user, RoomRole roomRole) {
        this.user = user;
        this.roomRole = roomRole;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "room_id", referencedColumnName = "room_id"),
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")
    })
    private RoomRole roomRole;

    @Column(name = "room_id", insertable = false, updatable = false)
    private Long roomId;

    @Column(name = "role_name", nullable = false, insertable = false, updatable = false)
    private String roleName;



    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


