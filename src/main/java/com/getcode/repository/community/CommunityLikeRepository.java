package com.getcode.repository.community;

import com.getcode.domain.community.CommunityLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    @Query("SELECT sl FROM CommunityLike sl WHERE sl.member.id = :memberId AND sl.community.id = :communityId")
    Optional<CommunityLike> findByMemberIdAndCommunityId(@Param("memberId") Long memberId,
                                                         @Param("communityId") Long communityId);
}
