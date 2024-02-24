package com.getcode.repository.community;

import com.getcode.domain.community.Community;
import com.getcode.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAll(Specification<Community> specification, Pageable pageable);

    List<Community> findAllByMemberId(Pageable pageable, Long id);

    @Query("select c from Community c where c.id in (select wc.community.id from WishCommunity wc where wc.member.id = :memberId)")
    List<Community> findAllByWishCommunityMemberId(Pageable pageable, @Param("memberId") Long memberId);
}
