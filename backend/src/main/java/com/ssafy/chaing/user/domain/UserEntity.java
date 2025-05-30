package com.ssafy.chaing.user.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = true)
    private String nickname;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "group_id")
    private Long groupId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_type", nullable = true)
    private RoleType roleType;

    @Column(name = "provider", nullable = true)
    private String provider;

    @Column(name = "provider_id", nullable = true)
    private String providerId;

    @Column(name = "fcm_token", nullable = true)
    private String fcmToken;

}
