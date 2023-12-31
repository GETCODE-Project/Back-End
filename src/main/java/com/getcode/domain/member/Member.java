package com.getcode.domain.member;

import com.getcode.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

//    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    @Column(nullable = false)
    private boolean emailVerified;

    private String imageUrl; // 프로필 이미지

//    @Builder
//    public Member(String email, String nickname, String password, Authority authority, boolean emailVerified) {
//        this.email = email;
//        this.nickname = nickname;
//        this.password = password;
//        this.authority = authority;
//        this.emailVerified = emailVerified;
//    }

    public void updateEmailVerified() {
        this.emailVerified = true;
    }

    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
