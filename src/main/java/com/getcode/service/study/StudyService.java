package com.getcode.service.study;

import static com.getcode.config.security.SecurityUtil.*;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.domain.study.StudyLike;
import com.getcode.domain.study.WishStudy;
import com.getcode.dto.study.StudyCommentRequestDto;
import com.getcode.dto.study.StudyCommentResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.dto.study.StudyLikeDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
import com.getcode.dto.study.StudyWishDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.DuplicateLikeException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundStudyException;
import com.getcode.exception.study.NotLikeException;
import com.getcode.exception.study.NotWishException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.study.StudyCommentRepository;
import com.getcode.repository.study.StudyLikeRepository;
import com.getcode.repository.study.StudyRepository;

import com.getcode.repository.study.WishStudyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final StudyLikeRepository studyLikeRepository;
    private final WishStudyRepository wishStudyRepository;

    @Transactional
    public StudyResponseDto createStudy(StudyRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Study study = studyRepository.save(req.toEntity(member));
        return StudyResponseDto.toDto(study);
    }

    // 특정 게시글 조회
    @Transactional
    public StudyInfoResponseDto findStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        study.increaseViews();
        return StudyInfoResponseDto.toDto(study);
    }

    // 모든 게시물 조회
    @Transactional(readOnly = true)
    public List<StudyResponseDto> findAllStudy() {
        List<Study> studies = studyRepository.findAllByOrderByModifiedDateDesc().orElseThrow(NotFoundStudyException::new);
        List<StudyResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyResponseDto.toDto(study)));
        return res;
    }

    // 특정 사용자가 작성한 게시물 조회
    @Transactional(readOnly = true)
    public List<StudyResponseDto> findAllStudyByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        List<Study> studies = member.getStudy();
        List<StudyResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyResponseDto.toDto(study)));
        return res;
    }

    // 스터디 수정
    @Transactional
    public StudyResponseDto editStudy(Long id, StudyEditDto req) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        study.editStudy(req);
        // 수정 요망
        return StudyResponseDto.toDto(studyRepository.save(study));
    }

    // 스터디 삭제
    @Transactional
    public void deleteStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (member.getId() != findMember.getId()) {
            throw new MatchMemberException();
        }

        studyRepository.delete(study);
    }

    // 스터디 댓글
    @Transactional
    public StudyCommentResponseDto addComment(StudyCommentRequestDto studyCommentRequestDto, Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        StudyComment res = studyCommentRepository.save(studyCommentRequestDto.toEntity(study, member));
        return StudyCommentResponseDto.toDto(res);
    }

    // 스터디 좋아요
    @Transactional
    public StudyInfoResponseDto likeStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);

        String owner = study.getMember().getEmail();

        if (owner.equals(getCurrentMemberEmail())) {
            throw new NotLikeException();
        }

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        Optional<StudyLike> like = studyLikeRepository.findByMemberIdAndStudyId(member.getId(), id);

        like.ifPresentOrElse(
                liked -> {
                    studyLikeRepository.delete(liked);
                    study.decreaseCount();
                },
                () -> {
                    study.increaseCount();
                    studyLikeRepository.save(StudyLikeDto.toEntity(member, study));
                }
        );


        return StudyInfoResponseDto.toDto(study);
    }

    // 스터디 찜하기
    @Transactional
    public void wishStudy(Long id) {

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);

        if (study.getMember().getId() == member.getId()) {
            throw new NotWishException();
        }

        Optional<WishStudy> wish = wishStudyRepository.findByMemberIdAndStudyId(member.getId(), id);

        wish.ifPresentOrElse(
                wishStudyRepository::delete,
                () -> wishStudyRepository.save(StudyWishDto.toEntity(member, study))
        );

    }


    /**
     * 최신순: modifiedDate
     * 인기순: count
     * */
    // 스터디 검색
    @Transactional(readOnly = true)
    public List<StudyResponseDto> searchStudy(String keyword, int pageNumber, String criteria) {
        Pageable pageable = PageRequest.of(pageNumber-1, 10,
                Sort.by(Sort.Direction.DESC, criteria));
        List<Study> studies = studyRepository.findByTitleOrContentContaining(keyword, pageable);
        List<StudyResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyResponseDto.toDto(study)));
        return res;
    }

}
