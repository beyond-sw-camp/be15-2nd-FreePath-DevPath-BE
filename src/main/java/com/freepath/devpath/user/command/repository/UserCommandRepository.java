package com.freepath.devpath.user.command.repository;

import com.freepath.devpath.user.command.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCommandRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    List<User> findByItNewsSubscription(String y);

    Optional<User> findByUserIdAndUserDeletedAtIsNull(Integer userId);

    Optional<User> findByEmailAndUserDeletedAtIsNull(String email);

    boolean existsByEmailAndUserDeletedAtIsNull(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email AND u.userDeletedAt IS NULL")
    int updatePasswordByEmail(String email, String password);
}