package com.getcode.service.mypage;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.study.Study;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentInfoResDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.project.ProjectRepository;
import com.getcode.repository.projectrecruitment.ProjectRecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberEmail;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectRecruitmentRepository projectRecruitmentRepository;

    @Transactional(readOnly = true)
    public List<ProjectInfoResponseDto> getMyProject(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        List<Project> projects = member.getProjects();

        List<ProjectInfoResponseDto> myProjectRes = new ArrayList<>();

        projects.forEach(project -> myProjectRes.add(new ProjectInfoResponseDto(project)));

        return myProjectRes;

    }


    @Transactional
    public List<ProjectInfoResponseDto> getMyWishProject(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Pageable pageable = PageRequest.of(page-1, size);

        List<Project> projects = projectRepository.findAllWishProjectByMemberId(member.getId(),pageable);

        List<ProjectInfoResponseDto> myWishProjectRes = new ArrayList<>();

        projects.forEach(project -> myWishProjectRes.add(new ProjectInfoResponseDto(project)));

        return myWishProjectRes;
    }

    public List<ProjectRecruitmentInfoResDto> getMyRecruitment(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        List<ProjectRecruitment> projectRecruitments = member.getProjectRecruitments();

        List<ProjectRecruitmentInfoResDto> myRecruitRes = new ArrayList<>();

        projectRecruitments.forEach(recruitment -> myRecruitRes.add(new ProjectRecruitmentInfoResDto(recruitment)));

        return myRecruitRes;

    }

    public List<ProjectRecruitmentInfoResDto> getMyWishRecruitment(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Pageable pageable = PageRequest.of(page-1, size);

        List<ProjectRecruitment> recruitments = projectRecruitmentRepository.findAllWishRecruitByMemberId(member.getId(),pageable);

        List<ProjectRecruitmentInfoResDto> myWishRecruitRes = new ArrayList<>();

        recruitments.forEach(recruitment -> myWishRecruitRes.add(new ProjectRecruitmentInfoResDto(recruitment)));

        return myWishRecruitRes;


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

}
