package com.getcode.domain.member;

import com.getcode.domain.common.BaseTimeEntity;
import com.getcode.domain.study.Study;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    @Column(unique = true)
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    @Column(nullable = false)
    private boolean emailVerified;

    private String imageUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Study> study = new ArrayList<>();

    public void updateEmailVerified() {
        this.emailVerified = true;
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateAuthority(Authority authority) {
        this.authority = authority;
    }
    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
