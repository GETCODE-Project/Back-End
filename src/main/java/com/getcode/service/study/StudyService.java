package com.getcode.service.study;

import static com.getcode.config.security.SecurityUtil.*;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.dto.study.StudyRequestDto;
import com.getcode.dto.study.StudyResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StudyResponseDto createStudy(StudyRequestDto req) {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Study study = studyRepository.save(req.toEntity(member));
        return StudyResponseDto.toDto(study);
    }
}
