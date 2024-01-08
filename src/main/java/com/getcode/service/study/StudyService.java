package com.getcode.service.study;

import static com.getcode.config.security.SecurityUtil.*;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyComment;
import com.getcode.dto.study.StudyCommentRequestDto;
import com.getcode.dto.study.StudyCommentResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.study.MatchMemberException;
import com.getcode.exception.study.NotFoundStudyException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.study.StudyCommentRepository;
import com.getcode.repository.study.StudyRepository;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyCommentRepository studyCommentRepository;

    @Transactional
    public StudyResponseDto createStudy(StudyRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Study study = studyRepository.save(req.toEntity(member));
        return StudyResponseDto.toDto(study);
    }

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public StudyInfoResponseDto findStudy(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        study.increaseViews();
        return StudyInfoResponseDto.toDto(studyRepository.save(study));
    }

    @Transactional(readOnly = true)
    public List<StudyResponseDto> findAllStudy() {
        List<Study> studies = studyRepository.findAllByOrderByModifiedDateDesc().orElseThrow(NotFoundStudyException::new);
        List<StudyResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyResponseDto.toDto(study)));
        return res;
    }

    @Transactional(readOnly = true)
    public List<StudyResponseDto> findAllStudyByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        List<Study> studies = member.getStudy();
        List<StudyResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyResponseDto.toDto(study)));
        return res;
    }

    @Transactional
    public StudyResponseDto editStudy(Long id, StudyEditDto req) {
        Study study = studyRepository.findById(id).orElseThrow(NotFoundStudyException::new);
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Member findMember = study.getMember();

        if (!member.equals(findMember)) {
            throw new MatchMemberException();
        }

        study.editStudy(req);

        return StudyResponseDto.toDto(studyRepository.save(study));
    }

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
}
