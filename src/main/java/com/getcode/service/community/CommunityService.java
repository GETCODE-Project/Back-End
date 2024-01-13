package com.getcode.service.community;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

import com.getcode.domain.community.Community;
import com.getcode.domain.member.Member;
import com.getcode.dto.community.CommunityRequestDto;
import com.getcode.dto.community.CreatedCommunityResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.community.CommunityRepository;
import com.getcode.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    // 게시판 생성
    @Transactional
    public CreatedCommunityResponseDto createCommunity(CommunityRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Community community = communityRepository.save(req.toEntity(member));
        return CreatedCommunityResponseDto.toDto(community);
    }
}
