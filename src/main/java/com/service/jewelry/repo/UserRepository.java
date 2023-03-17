package com.service.jewelry.repo;

import com.service.jewelry.model.User;
import com.service.jewelry.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    UserEntity findByUsername(String username);
}
