package com.getcode.service.community;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityComment;
import com.getcode.domain.community.CommunityLike;
import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.dto.community.CommunityCommentRequestDto;
import com.getcode.dto.community.CommunityCommentResponseDto;
import com.getcode.dto.community.CommunityEditDto;
import com.getcode.dto.community.CommunityLikeDto;
import com.getcode.dto.community.CommunityRequestDto;
import com.getcode.dto.community.CommunityResponseDto;
import com.getcode.dto.community.CreatedCommunityResponseDto;
import com.getcode.exception.community.NotFoundCommunityException;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundCommentException;
import com.getcode.exception.study.NotLikeException;
import com.getcode.repository.community.CommunityCommentRepository;
import com.getcode.repository.community.CommunityLikeRepository;
import com.getcode.repository.community.CommunityRepository;
import com.getcode.repository.member.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityLikeRepository communityLikeRepository;
    private final CommunityCommentRepository communityCommentRepository;

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
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);
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
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = community.getMember();

        if (member.getId() != findMember.getId()) {
            throw new MatchMemberException();
        }

        communityRepository.delete(community);
    }

    // 특정 게시글 조회 V
    @Transactional
    public CommunityResponseDto findCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(NotFoundCommunityException::new);
        community.increaseViews();
        return CommunityResponseDto.toDto(community);
    }

    // 게시글 좋아요
    @Transactional
    public CommunityResponseDto likeCommunity(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);

        String owner = community.getMember().getEmail();

        if (owner.equals(getCurrentMemberEmail())) {
            throw new NotLikeException();
        }

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        Optional<CommunityLike> like = communityLikeRepository.findByMemberIdAndCommunityId(member.getId(), id);

        like.ifPresentOrElse(
                liked -> {
                    communityLikeRepository.delete(liked);
                    community.decreaseCount();
                },
                () -> {
                    community.increaseCount();
                    communityLikeRepository.save(CommunityLikeDto.toEntity(member, community));
                }
        );

        return CommunityResponseDto.toDto(community);
    }

    // 스터디 댓글
    @Transactional
    public CommunityCommentResponseDto addComment(CommunityCommentRequestDto req, Long id) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        CommunityComment res = communityCommentRepository.save(req.toEntity(community, member));
        return CommunityCommentResponseDto.toDto(res);
    }

    // 댓글 수정
    @Transactional
    public CommunityCommentResponseDto editComment(CommunityCommentRequestDto req, Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        communityComment.editComment(req.getContent());
        return CommunityCommentResponseDto.toDto(communityComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        communityCommentRepository.delete(communityComment);
    }
}
