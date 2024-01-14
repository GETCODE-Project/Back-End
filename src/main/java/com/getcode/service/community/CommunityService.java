package com.getcode.service.community;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

import com.getcode.domain.community.Community;
import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.dto.community.CommunityEditDto;
import com.getcode.dto.community.CommunityRequestDto;
import com.getcode.dto.community.CommunityResponseDto;
import com.getcode.dto.community.CreatedCommunityResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundStudyException;
import com.getcode.repository.community.CommunityRepository;
import com.getcode.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.List;
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

    // 특정 사용자가 작성한 게시판 조회
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> findAllCommunityByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        List<Community> communities = member.getCommunity();
        List<CommunityResponseDto> res = new ArrayList<>();
        communities.forEach(community -> res.add(CommunityResponseDto.toDto(community)));
        return res;
    }

    // 게시판 수정
    @Transactional
    public CommunityResponseDto editCommunity(Long id, CommunityEditDto req) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = community.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        community.editCommunity(req);

        return CommunityResponseDto.toDto(community);
    }

    // 게시글 삭제
    @Transactional
    public void deleteCommunity(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundStudyException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = community.getMember();

        if (member.getId() != findMember.getId()) {
            throw new MatchMemberException();
        }

        communityRepository.delete(community);
    }
}
