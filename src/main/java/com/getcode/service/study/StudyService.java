package com.getcode.service.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.domain.study.StudyLike;
import com.getcode.domain.study.WishStudy;
import com.getcode.dto.study.util.StudyFieldDto;
import com.getcode.dto.study.util.StudyWishDto;
import com.getcode.dto.study.request.StudyCommentRequestDto;
import com.getcode.dto.study.util.StudyLikeDto;
import com.getcode.dto.study.request.StudyRequestDto;
import com.getcode.dto.study.response.CreatedStudyResponseDto;
import com.getcode.dto.study.response.StudyCommentResponseDto;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.dto.study.util.StudySpecification;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.*;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.study.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final StudyLikeRepository studyLikeRepository;
    private final WishStudyRepository wishStudyRepository;
    private final StudyFieldRepository studyFieldRepository;

    @Transactional
    public CreatedStudyResponseDto createStudy(StudyRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        List<String> fields = req.getFields();

        Study study = studyRepository.save(req.toEntity(member));

        for (String subject : fields) {
            studyFieldRepository.save(StudyFieldDto.toEntity(study, subject));
        }

        return CreatedStudyResponseDto.toDto(study, member, fields);
    }

    // 특정 게시글 조회
    @Transactional
    public StudyInfoResponseDto findStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(NotFoundStudyException::new);
        study.increaseViews();
        return StudyInfoResponseDto.toDto(study);
    }

    // 모든 게시물 조회
    @Transactional(readOnly = true)
    public List<StudyInfoResponseDto> findAllStudy() {

        List<Study> studies = studyRepository.findAllByOrderByModifiedDateDesc()
                .orElseThrow(NotFoundStudyException::new);

        List<StudyInfoResponseDto> res = new ArrayList<>();

        for (Study study : studies) {
            memberRepository.findById(study.getMember().getId())
                    .orElseThrow(NotFoundMemberException::new);
            res.add(StudyInfoResponseDto.toDto(study));
        }

        return res;
    }

    // 스터디 수정
    @Transactional
    public StudyInfoResponseDto editStudy(Long id, StudyRequestDto req) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        study.editStudy(req);
        // 수정 요망
        return StudyInfoResponseDto.toDto(studyRepository.save(study));
    }

    // 스터디 삭제
    @Transactional
    public void deleteStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (!member.getId().equals(findMember.getId())) {
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

    // 댓글 수정
    @Transactional
    public StudyCommentResponseDto editComment(StudyCommentRequestDto studyCommentRequestDto, Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        studyComment.editComment(studyCommentRequestDto.getContent());
        return StudyCommentResponseDto.toDto(studyComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        studyCommentRepository.delete(studyComment);
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

        if (study.getMember().getId().equals(member.getId())) {
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
    public List<StudyInfoResponseDto> searchStudy(String keyword, String siDo, String guGun, Boolean recruitment,
                                                  Boolean online, Integer year, List<String> fields,
                                                  int page, int size, String criteria) {
        Specification<Study> spec = (root, query, criteriaBuilder) -> null;

        if (keyword != null) {
            spec = spec.and(StudySpecification.equalsKeyword(keyword));
        }

        if (siDo != null) {
            spec = spec.and(StudySpecification.equalsSiDo(siDo));
        }

        if (guGun != null) {
            spec = spec.and(StudySpecification.equalsGuGun(guGun));
        }

        if (recruitment != null) {
            spec = spec.and(StudySpecification.equalsRecruitment(recruitment));
        }

        if (online != null) {
            spec = spec.and(StudySpecification.equalsOnline(online));
        }

        if (year != null) {
            spec = spec.and(StudySpecification.equalsYear(year));
        }

        if (fields != null) {
            spec = spec.and(StudySpecification.containsFields(fields));
        }

        Pageable pageable = PageRequest.of(page-1, size,
                Sort.by(Sort.Direction.DESC, "modifiedDate"));

        if (criteria != null) {
            pageable = PageRequest.of(page-1, size,
                    Sort.by(Sort.Direction.DESC, criteria));
        }

        Page<Study> studies = studyRepository.findAll(spec, pageable);
        List<StudyInfoResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyInfoResponseDto.toDto(study)));
        return res;
    }

}
