package com.example.Quiz.repository;

import com.example.Quiz.model.UserAuthProvider;
import com.example.Quiz.model.enums.Providers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, Long> {
    Optional<UserAuthProvider> findByProviderAndProviderUserId(Providers provider, String providerUserId);
    Boolean existsByProviderAndProviderUserId(Providers provider, String providerUserId);
}
