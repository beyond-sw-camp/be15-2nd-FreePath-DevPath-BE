package com.freepath.devpath.user.command.repository;

import com.freepath.devpath.user.command.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    List<User> findByItNewsSubscription(String y);
}