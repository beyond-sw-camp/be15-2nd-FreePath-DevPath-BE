package com.freepath.devpath.user.command.entity;

import com.freepath.devpath.user.command.dto.UserModifyRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String loginId;

    private String password;

    private String email;

    private String userName;

    private String nickname;

    private String loginMethod;

    private String developerPersonality;

    private String itNewsSubscription;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    // insert 시 JPA가 이 컬럼에 값을 명시하지 않음
    @Column(insertable = false)
    private String isUserRestricted;

    @Column(insertable = false)
    private Date userRegisteredAt;

    private Date userDeletedAt;

    // userRole값이 null일 경우는 Default값인 USER로 설정하고, 아닌 경우 ADMIN으로 설정할 수 있도록 하기 위함
    @PrePersist
    public void setDefaultUserRole() {
        if (userRole == null) {
            userRole = UserRole.USER;
        }
    }

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void update(UserModifyRequest request, String encodedPassword) {
        this.password = encodedPassword;
        this.email = request.getEmail();
        this.nickname = request.getNickname();
    }
}
