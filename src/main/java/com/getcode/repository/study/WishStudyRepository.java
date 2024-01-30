package com.getcode.repository.study;

import com.getcode.domain.study.WishStudy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface WishStudyRepository extends JpaRepository<WishStudy, Long> {
    Optional<WishStudy> findByMemberIdAndStudyId(@Param("memberId") Long memberId, @Param("studyId") Long studyId);
}
