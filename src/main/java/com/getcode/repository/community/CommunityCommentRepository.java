package com.getcode.repository.community;

import com.getcode.domain.community.CommunityComment;
import com.getcode.dto.community.response.CommunityCommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findByCommunityId(Long id);
}
