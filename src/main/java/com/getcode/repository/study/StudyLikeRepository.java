package com.getcode.repository.study;

import com.getcode.domain.study.StudyLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyLikeRepository extends JpaRepository<StudyLike, Long> {

    @Query("SELECT sl FROM StudyLike sl WHERE sl.member.id = :memberId AND sl.study.id = :studyId")
    Optional<StudyLike> findByMemberIdAndStudyId(@Param("memberId") Long memberId, @Param("studyId") Long studyId);

}
