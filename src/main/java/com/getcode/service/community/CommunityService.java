package com.getcode.service.community;

import com.getcode.domain.community.Community;
import com.getcode.domain.community.CommunityComment;
import com.getcode.domain.community.CommunityLike;
import com.getcode.domain.community.WishCommunity;
import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.dto.community.requset.CommunityCommentRequestDto;
import com.getcode.dto.community.requset.CommunityLikeDto;
import com.getcode.dto.community.requset.CommunityRequestDto;
import com.getcode.dto.community.response.CommunityCommentResponseDto;
import com.getcode.dto.community.response.CommunityDetailResponseDto;
import com.getcode.dto.community.response.CommunityInfoResponseDto;
import com.getcode.dto.community.response.CreatedCommunityResponseDto;
import com.getcode.dto.community.util.CommunitySpecification;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.dto.study.util.StudySpecification;
import com.getcode.exception.community.NotFoundCommunityException;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundCommentException;
import com.getcode.repository.community.CommunityCommentRepository;
import com.getcode.repository.community.CommunityLikeRepository;
import com.getcode.repository.community.CommunityRepository;
import com.getcode.repository.community.WishCommunityRepository;
import com.getcode.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityLikeRepository communityLikeRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final WishCommunityRepository wishCommunityRepository;

    // 게시판 생성
    @Transactional
    public CreatedCommunityResponseDto createCommunity(CommunityRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Community community = communityRepository.save(req.toEntity(member));
        return CreatedCommunityResponseDto.toDto(community);
    }

    // 커뮤니티 게시글 검색 및 태그 조회
    public List<CommunityInfoResponseDto> searchCommunity(String keyword,
                                                          String category,
                                                          String sort,
                                                          int page, int size){
        Specification<Community> spec = (root, query, criteriaBuilder) -> null;

        if (keyword != null) {
            spec = spec.and(CommunitySpecification.equalsKeyword(keyword));
        }

        if (category != null) {
            spec = spec.and(CommunitySpecification.equalsCategory(category));
        }

        Sort sortCriteria;
        if(sort.equals("pastOrder")){
            sortCriteria = Sort.by(Sort.Direction.ASC, "modifiedDate");
        } else if (sort.equals("likeCnt")) {
            sortCriteria = Sort.by(Sort.Direction.DESC, "likeCnt");
        } else {
            sortCriteria = Sort.by(Sort.Direction.DESC, "modifiedDate");
        }
        Page<Community> communities = communityRepository
                .findAll(spec, PageRequest.of(page-1, size, sortCriteria));
        String currentMemberEmail = getCurrentMemberEmail();
        Member member = memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(NotFoundMemberException::new);
        Long memberId = member.getId();

        return communities.map(c -> {
            boolean likeCond = communityLikeRepository
                    .findByMemberIdAndCommunityId(memberId, c.getId()).isPresent();
            boolean wishCond = wishCommunityRepository
                    .findByMemberIdAndCommunityId(memberId, c.getId()).isPresent();
            return CommunityInfoResponseDto.toDto(c,likeCond, wishCond);
        }).toList();
    }

    // 특정 사용자가 작성한 게시판 조회
    @Transactional(readOnly = true)
    public List<CommunityInfoResponseDto> findAllCommunityByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail())
                .orElseThrow(NotFoundMemberException::new);
        List<Community> communities = member.getCommunity();

        Long memberId = member.getId();

        return communities.stream()
                .map(c -> {
                    boolean checkLike = communityLikeRepository
                            .findByMemberIdAndCommunityId(memberId, c.getId()).isPresent();
                    boolean checkWish = wishCommunityRepository
                            .findByMemberIdAndCommunityId(memberId, c.getId()).isPresent();
                    return CommunityInfoResponseDto.toDto(c,checkLike,checkWish);
                }).toList();
    }

    // 게시판 수정
    @Transactional
    public void editCommunity(Long id, CommunityRequestDto req) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = community.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        community.editCommunity(req);
    }

    // 게시글 삭제
    @Transactional
    public void deleteCommunity(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = community.getMember();

        if (!member.getId().equals(findMember.getId())) {
            throw new MatchMemberException();
        }

        communityRepository.delete(community);
    }

    // 특정 게시글 조회
    @Transactional
    public CommunityDetailResponseDto findCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(NotFoundCommunityException::new);
        community.increaseViews();

        if (!getCurrentMemberEmail().equals("false")){
            List<CommunityCommentResponseDto> list = community.getComments().stream()
                    .map(cc -> CommunityCommentResponseDto.toDto(cc, false)).toList();
            return CommunityDetailResponseDto
                    .toDto(community,list,false, false, false);
        }

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow();
        Long communityId = community.getId();
        Long memberId = member.getId();
        List<CommunityCommentResponseDto> dtos = community
                .getComments().stream()
                .map(cc -> CommunityCommentResponseDto.toDto(cc, cc.getMember().equals(member)))
                .toList();
        boolean checkLike = communityLikeRepository
                .findByMemberIdAndCommunityId(memberId, communityId).isPresent();
        boolean checkWish = wishCommunityRepository
                .findByMemberIdAndCommunityId(memberId, communityId).isPresent();
        boolean isWriter = community.getMember().equals(member);

        return CommunityDetailResponseDto.toDto(community,dtos,checkLike, checkWish, isWriter);
    }

    // 게시글 좋아요
    @Transactional
    public String likeCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(NotFoundCommunityException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail())
                .orElseThrow(NotFoundMemberException::new);

        Optional<CommunityLike> likeInfo = communityLikeRepository
                .findByMemberIdAndCommunityId(member.getId(), id);
        if(likeInfo.isPresent()) {
            communityLikeRepository.delete(likeInfo.get());
            community.decreaseCount();
            return "좋아요 취소 성공";
        }
        community.increaseCount();
        communityLikeRepository.save(CommunityLikeDto.toEntity(member, community));
        return "좋아요 성공";
    }

    @Transactional
    public String wishCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(NotFoundCommunityException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail())
                .orElseThrow(NotFoundMemberException::new);

        Optional<WishCommunity> wishInfo = wishCommunityRepository
                .findByMemberIdAndCommunityId(member.getId(), id);
        if(wishInfo.isPresent()) {
            wishCommunityRepository.delete(wishInfo.get());
            return "찜하기 취소 성공";
        }
        wishCommunityRepository.save(WishCommunity.createWishCommunity(member, community));
        return "찜하기 성공";
    }

    // 커뮤니티 댓글 등록
    @Transactional
    public void addComment(CommunityCommentRequestDto req, Long id) {
        Community community = communityRepository.findById(id).orElseThrow(NotFoundCommunityException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        communityCommentRepository.save(req.toEntity(community, member));
    }

    // 댓글 수정
    @Transactional
    public void editComment(CommunityCommentRequestDto req, Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        communityComment.editComment(req.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        communityCommentRepository.delete(communityComment);
    }
}
