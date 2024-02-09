package com.getcode.repository.community;

import com.getcode.domain.community.Community;
import com.getcode.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAll(Specification<Community> specification, Pageable pageable);
}
