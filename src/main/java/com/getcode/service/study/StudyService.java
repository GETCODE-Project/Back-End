package com.getcode.service.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.domain.study.StudyLike;
import com.getcode.domain.study.WishStudy;
import com.getcode.dto.study.response.StudyDetailResponseDto;
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

        for (String field : fields) {
            studyFieldRepository.save(StudyFieldDto.toEntity(study, field));
        }

        return CreatedStudyResponseDto.toDto(study, member, fields);
    }

    // 특정 게시글 조회
    @Transactional
    public StudyDetailResponseDto findStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(NotFoundStudyException::new);
        String currentMemberEmail = getCurrentMemberEmail();
        if(!currentMemberEmail.equals("false")) {
            study.increaseViews();
            List<StudyCommentResponseDto> dtos = study.getComments().stream()
                    .map(sc -> StudyCommentResponseDto.toDto(sc, false)).toList();
            return StudyDetailResponseDto.toDto(study,dtos,false,false,false);
        }

        Member member = memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(NotFoundMemberException::new);
        Long memberId = member.getId();
        Long studyId = study.getId();
        List<StudyCommentResponseDto> dtos = study.getComments().stream()
                .map(sc -> StudyCommentResponseDto.toDto(sc, sc.getMember().equals(member)))
                .toList();
        boolean likeCond = studyLikeRepository
                .findByMemberIdAndStudyId(memberId, studyId).isPresent();
        boolean wishCond = wishStudyRepository
                .findByMemberIdAndStudyId(memberId, studyId).isPresent();
        boolean isWriter = study.getMember().equals(member);

        study.increaseViews();
        return StudyDetailResponseDto.toDto(study, dtos, likeCond, wishCond, isWriter);
    }

    // 스터디 수정
    @Transactional
    public StudyInfoResponseDto editStudy(Long id, StudyRequestDto req) {
        Study study = studyRepository.findById(id)
                .orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail())
                .orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        Long memberId = member.getId();
        Long studyId = study.getId();
        boolean likeCond = studyLikeRepository
                .findByMemberIdAndStudyId(memberId, studyId).isPresent();
        boolean wishCond = wishStudyRepository
                .findByMemberIdAndStudyId(memberId, studyId).isPresent();

        List<String> fields = req.getFields();

        study.editStudy(req);

        for (String field : fields) {
            studyFieldRepository.save(StudyFieldDto.toEntity(study, field));
        }

        return StudyInfoResponseDto
                .toDto(studyRepository.save(study),likeCond,wishCond);
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
        return StudyCommentResponseDto.toDto(res,true);
    }

    // 댓글 수정
    @Transactional
    public StudyCommentResponseDto editComment(StudyCommentRequestDto studyCommentRequestDto, Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
        studyComment.editComment(studyCommentRequestDto.getContent());
        return StudyCommentResponseDto.toDto(studyComment,true);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        StudyComment studyComment = studyCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
        studyCommentRepository.delete(studyComment);
    }

    // 스터디 좋아요
    @Transactional
    public void likeStudy(Long id) {

        String owner = studyRepository.findById(id)
                .orElseThrow(NotFoundStudyException::new).getMember().getEmail();

        if (owner.equals(getCurrentMemberEmail())) {
            throw new NotLikeException();
        }

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        Optional<StudyLike> like = studyLikeRepository.findByMemberIdAndStudyId(member.getId(), id);

        like.ifPresentOrElse(
                liked -> {
                    studyLikeRepository.delete(liked);
                    studyRepository.findById(id).orElseThrow(NotFoundStudyException::new).decreaseCount();
                },
                () -> {
                    studyRepository.findById(id).orElseThrow(NotFoundStudyException::new).increaseCount();
                    studyLikeRepository.save(StudyLikeDto.toEntity(member, studyRepository.findById(id).orElseThrow(NotFoundStudyException::new)));
                }
        );
    }

    // 스터디 찜하기
    @Transactional
    public void wishStudy(Long id) {

        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);

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
                                                  int page, int size, String sort) {
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

        Sort sortCriteria;
        if(sort.equals("pastOrder")){
            sortCriteria = Sort.by(Sort.Direction.ASC, "modifiedDate");
        } else if (sort.equals("likeCnt")) {
            sortCriteria = Sort.by(Sort.Direction.DESC, "likeCnt");
        } else {
            sortCriteria = Sort.by(Sort.Direction.DESC, "modifiedDate");
        }

        Page<Study> studies = studyRepository
                .findAll(spec, PageRequest.of(page-1, size, sortCriteria));


        String currentMemberEmail = getCurrentMemberEmail();
        if (currentMemberEmail.equals("false")) {
            return studies.map(s -> StudyInfoResponseDto.toDto(s,false, false))
                    .toList();
        }
        Member member = memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(NotFoundMemberException::new);
        Long memberId = member.getId();

        return studies.map(s -> {
            boolean likeCond = studyLikeRepository
                    .findByMemberIdAndStudyId(memberId, s.getId()).isPresent();
            boolean wishCond = wishStudyRepository
                    .findByMemberIdAndStudyId(memberId, s.getId()).isPresent();
            return StudyInfoResponseDto.toDto(s,likeCond, wishCond);
        }).toList();
    }

}
