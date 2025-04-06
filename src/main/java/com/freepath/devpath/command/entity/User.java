package com.freepath.devpath.command.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(columnDefinition = "CHAR(4)")
    private String developerPersonality;

    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private String itNewsSubscription = "N";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private String isUserRestricted = "N";

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date userRegisteredAt;

    @Column(nullable = true)
    private Date userDeletedAt;

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
