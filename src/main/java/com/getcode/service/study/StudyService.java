package com.getcode.service.study;

import static com.getcode.config.security.SecurityUtil.*;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.domain.study.StudyLike;
import com.getcode.domain.study.StudySpecification;
import com.getcode.domain.study.StudySubject;
import com.getcode.domain.study.WishStudy;
import com.getcode.dto.study.CreatedStudyResponseDto;
import com.getcode.dto.study.StudyCommentRequestDto;
import com.getcode.dto.study.StudyCommentResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.dto.study.StudyLikeDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudySubjectDto;
import com.getcode.dto.study.StudyWishDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundCommentException;
import com.getcode.exception.study.NotFoundStudyException;
import com.getcode.exception.study.NotLikeException;
import com.getcode.exception.study.NotWishException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.study.StudyCommentRepository;
import com.getcode.repository.study.StudyLikeRepository;
import com.getcode.repository.study.StudyRepository;

import com.getcode.repository.study.StudySubjectRepository;
import com.getcode.repository.study.WishStudyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final StudySubjectRepository studySubjectRepository;

    @Transactional
    public CreatedStudyResponseDto createStudy(StudyRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        List<String> subjects = req.getSubjects();

        Study study = studyRepository.save(req.toEntity(member));

        for (String subject : subjects) {
            studySubjectRepository.save(StudySubjectDto.toEntity(study, subject));
        }

        return CreatedStudyResponseDto.toDto(study, member, subjects);
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
    public List<StudyInfoResponseDto> findAllStudy() {

        List<Study> studies = studyRepository.findAllByOrderByModifiedDateDesc().orElseThrow(NotFoundStudyException::new);

        List<StudyInfoResponseDto> res = new ArrayList<>();

        for (Study study : studies) {
            Member member = memberRepository.findById(study.getMember().getId()).orElseThrow(NotFoundMemberException::new);
            res.add(StudyInfoResponseDto.toDto(study));
        }

        return res;
    }

    // 특정 사용자가 작성한 게시물 조회
    @Transactional(readOnly = true)
    public List<StudyInfoResponseDto> findAllStudyByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        List<Study> studies = member.getStudy();
        List<StudyInfoResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyInfoResponseDto.toDto(study)));
        return res;
    }

    // 스터디 수정
    @Transactional
    public StudyInfoResponseDto editStudy(Long id, StudyEditDto req) {
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

    // 댓글 수정
    @Transactional
    public StudyCommentResponseDto editComment(StudyCommentRequestDto studyCommentRequestDto, Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
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
    public List<StudyInfoResponseDto> searchStudy(String keyword, String region, Boolean recruitment,
                                                  Boolean online, Integer year, List<String> subjects,
                                                  Integer pageNumber, String criteria) {
        Specification<Study> spec = (root, query, criteriaBuilder) -> null;

        if (keyword != null) {
            spec = spec.and(StudySpecification.equalsKeyword(keyword));
        }

        if (region != null) {
            spec = spec.and(StudySpecification.equalsRegion(region));
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

        if (subjects != null) {
            spec = spec.and(StudySpecification.containsSubjects(subjects));
        }

        Pageable pageable = PageRequest.of(pageNumber-1, 10,
                Sort.by(Sort.Direction.DESC, "modifiedDate"));

        if (criteria != null) {
            pageable = PageRequest.of(pageNumber-1, 10,
                    Sort.by(Sort.Direction.DESC, criteria));
        }

        Page<Study> studies = studyRepository.findAll(spec, pageable);
        List<StudyInfoResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyInfoResponseDto.toDto(study)));
        return res;
    }

}
