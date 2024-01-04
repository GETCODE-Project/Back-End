package com.getcode.service.project;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectImage;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.project.ProjectImageRepository;
import com.getcode.repository.project.ProjectRepository;
import com.getcode.repository.project.ProjectStackRepository;
import com.getcode.repository.project.ProjectSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberId;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectSubjectRepository projectSubjectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public int deleteProject(Long id, long memberId) {

        Optional<Project> existedProject = projectRepository.findById(id);

        return existedProject
                .filter(project -> project.getMember() != null && project.getMember().getId().equals(memberId))
                .map(project -> {
                    projectRepository.deleteById(id);
                    return 1; // 삭제 성공
                })
                .orElseGet(() -> -1); // 해당 id에 해당하는 프로젝트가 없음

    }



    @Transactional
    public void insertProject(ProjectRequestDto projectRequestDto) {

        Member member = memberRepository.findById(Long.parseLong(getCurrentMemberId())).orElseThrow(NotFoundMemberException::new);

        Project project = projectRequestDto.toProjectEntity(member);

        //해당 엔티티들에 project_id가 안들어가서 만들어 놓은 임시방편 리팩토링 필요
        List<ProjectTech> techStacks = projectRequestDto.getTechStackList();
        List<ProjectImage> projectImageUrls = projectRequestDto.getImageUrls();
        List<ProjectSubject> projectSubjects = projectRequestDto.getProjectSubjects();

        synchronized (techStacks) {
            Iterator<ProjectTech> techIterator = techStacks.iterator();
            while (techIterator.hasNext()) {
                ProjectTech projectTech = techIterator.next();
                project.stackAdd(projectTech.getTechStack(projectTech.getTechStack(), project));
            }
        }

        //ProjectImageUrls 처리
        synchronized (projectImageUrls) {
            Iterator<ProjectImage> imageIterator = projectImageUrls.iterator();
            while (imageIterator.hasNext()) {
                ProjectImage projectImage = imageIterator.next();
                project.projectImageAdd(projectImage.getImage(projectImage.getImageUrl(), project));
            }
        }

        //ProjectSubjects 처리
        synchronized (projectSubjects) {
            Iterator<ProjectSubject> subjectIterator = projectSubjects.iterator();
            while (subjectIterator.hasNext()) {
                ProjectSubject projectSubject = subjectIterator.next();
                project.projectSubjectAdd(projectSubject.getSubject(projectSubject.getSubject(), project));
            }
        }


        projectRepository.save(project);


    }

    @Transactional(readOnly = true)
    public Boolean checkGithubUrlDuplication(String githubUrl) {
        Boolean urlDuplicate = projectRepository.existsByGithubUrl(githubUrl);
        return urlDuplicate;
    }
}
