package com.getcode.repository.community;

import com.getcode.domain.community.WishCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishCommunityRepository extends JpaRepository<WishCommunity, Long> {
    @Query("SELECT wc FROM WishCommunity wc WHERE wc.member.id = :memberId AND wc.community.id = :communityId")
    Optional<WishCommunity> findByMemberIdAndCommunityId(@Param("memberId") Long memberId,
                                                         @Param("communityId") Long communityId);
}
