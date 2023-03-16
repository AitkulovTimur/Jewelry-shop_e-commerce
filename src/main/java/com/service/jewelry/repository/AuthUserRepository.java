package com.service.jewelry.repository;

import com.service.jewelry.model.auth.user.AuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthUserRepository extends CrudRepository<AuthUserEntity, UUID> {

    boolean existsByEmail(String email);
}
