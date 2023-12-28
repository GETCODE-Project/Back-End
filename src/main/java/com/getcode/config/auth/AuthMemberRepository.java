package com.getcode.config.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMemberRepository extends JpaRepository<AuthMember, Long> {
    // findByEmailAndProvider 메서드를 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단
    Optional<AuthMember> findByEmailAndProvider(String email, String provider);
}
