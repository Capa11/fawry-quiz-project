package com.example.Quiz.model;

import com.example.Quiz.model.enums.Providers;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(
        name = "user_auth_providers",
        indexes = {
                @Index(name = "idx_provider_user_id", columnList = "provider_user_id"),
        }
)@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
public class UserAuthProvider {
    @Id
    private Long id;
    @OneToOne( optional = false)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private Providers provider;

    @Column(name = "provider_user_id",columnDefinition = "text")
    private String providerUserId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserAuthProvider(User user, Providers provider, String providerUserId) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }

}
