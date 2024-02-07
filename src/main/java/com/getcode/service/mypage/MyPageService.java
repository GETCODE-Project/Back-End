package com.getcode.service.mypage;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.study.Study;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentInfoResDto;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.project.ProjectRepository;
import com.getcode.repository.projectrecruitment.ProjectRecruitmentRepository;
import com.getcode.service.project.ProjectService;
import com.getcode.service.projectrecruitment.ProjectRecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ProjectRecruitmentService projectRecruitmentService;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<ProjectInfoResponseDto> getMyProject(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedDate");

        Pageable pageable = PageRequest.of(page-1, size, sort);

        List<Project> projects = projectRepository.findAllByMemberId(member.getId(),pageable);

        List<ProjectInfoResponseDto> myProjectRes = new ArrayList<>();

        projects.forEach(project -> {

            ProjectInfoResponseDto dto = new ProjectInfoResponseDto(project);

                dto.setCheckLike(projectService.isProjectLikedByUser(project.getId(), member.getId()));
                dto.setCheckWish(projectService.isProjectWishedByUser(project.getId(), member.getId()));

            myProjectRes.add(dto);
        });

        return myProjectRes;

    }


    @Transactional
    public List<ProjectInfoResponseDto> getMyWishProject(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedDate");

        Pageable pageable = PageRequest.of(page-1, size, sort);

        List<Project> projects = projectRepository.findAllWishProjectByMemberId(member.getId(),pageable);

        List<ProjectInfoResponseDto> myWishProjectRes = new ArrayList<>();

        projects.forEach(project -> {
            ProjectInfoResponseDto dto = new ProjectInfoResponseDto(project);

            dto.setCheckLike(projectService.isProjectLikedByUser(project.getId(), member.getId()));
            dto.setCheckWish(projectService.isProjectWishedByUser(project.getId(), member.getId()));

            myWishProjectRes.add(dto);
        });

        return myWishProjectRes;
    }

    public List<ProjectRecruitmentInfoResDto> getMyRecruitment(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedDate");

        Pageable pageable = PageRequest.of(page-1, size, sort);

        List<ProjectRecruitment> projectRecruitments = projectRecruitmentRepository.findAllByMemberId(member.getId(),pageable);

        List<ProjectRecruitmentInfoResDto> myRecruitRes = new ArrayList<>();

        projectRecruitments.forEach(recruitment -> {

            ProjectRecruitmentInfoResDto dto = new ProjectRecruitmentInfoResDto(recruitment);

            dto.setCheckLike(projectRecruitmentService.isRecruitmentLikedByUser(recruitment.getId(), member.getId()));
            dto.setCheckWish(projectRecruitmentService.isRecruitmentWishedByUser(recruitment.getId(), member.getId()));

            myRecruitRes.add(dto);
        });

        return myRecruitRes;

    }

    public List<ProjectRecruitmentInfoResDto> getMyWishRecruitment(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedDate");

        Pageable pageable = PageRequest.of(page-1, size, sort);

        List<ProjectRecruitment> recruitments = projectRecruitmentRepository.findAllWishRecruitByMemberId(member.getId(),pageable);

        List<ProjectRecruitmentInfoResDto> myWishRecruitRes = new ArrayList<>();

        recruitments.forEach(recruitment ->{

            ProjectRecruitmentInfoResDto dto = new ProjectRecruitmentInfoResDto(recruitment);

                dto.setCheckLike(projectRecruitmentService.isRecruitmentLikedByUser(recruitment.getId(), member.getId()));
                dto.setCheckWish(projectRecruitmentService.isRecruitmentWishedByUser(recruitment.getId(), member.getId()));

            myWishRecruitRes.add(dto);
        });

        return myWishRecruitRes;


    }

    // 특정 사용자가 작성한 게시물 조회
    @Transactional(readOnly = true)
    public List<StudyInfoResponseDto> findAllStudyByMember() {
        Member member = memberRepository.findByEmail(getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        List<Study> studies = member.getStudy();
        List<StudyInfoResponseDto> res = new ArrayList<>();
        studies.forEach(study -> res.add(StudyInfoResponseDto.toDto(study,false,false)));
        return res;
    }

}
