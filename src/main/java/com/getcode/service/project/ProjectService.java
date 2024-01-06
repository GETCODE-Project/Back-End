package com.getcode.service.project;

import com.getcode.config.s3.S3Service;
import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.*;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.NotFoundProjectException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.project.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static com.getcode.config.security.SecurityUtil.getCurrentMemberId;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectSubjectRepository projectSubjectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MemberRepository memberRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final S3Service s3Service;

    @Transactional
    public int deleteProject(Long id, long memberId) {

        Optional<Project> existedProject = projectRepository.findById(id);

        return existedProject
                .filter(project -> project.getMember() != null && project.getMember().getId().equals(memberId)) //로그인한 유저와 글 쓴 유저 일치하는지 확인
                .map(project -> {
                    //S3파일 삭제하기 위해 받은 값을 String으로 변환
                    List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(id);
                    List<String> imageUrls = new ArrayList<>();

                    for(ProjectImage projectImage : projectImages){
                        String url = projectImage.getImageUrl();
                        imageUrls.add(url);
                    }

                    //변환한 url S3에서 삭제
                    for(String imageUrl : imageUrls){

                        String[] files = extractPathFromImageUrl(imageUrl);

                       String result = s3Service.deleteFile(files[0], files[1]);

                    }

                    projectRepository.deleteById(id);
                    return 1; // 삭제 성공
                })
                .orElseGet(() -> -1); // 해당 id에 해당하는 프로젝트가 없음

    }


    //이미지 Url 제거를 위해 폴더 이름과 UUid 파일명을 분리해주는 메소드
    private String[] extractPathFromImageUrl(String imageUrl){

        String[] parts = imageUrl.split("/");

        if(parts.length >= 8){
                                // "/" 기준으로 배열생성 후에 공통적으로 붙는 buketName 다음부터 다시 합치기
            String folderPath = String.join("/", Arrays.copyOfRange(parts,4, parts.length -1));
            String fileName = parts[parts.length-1];

            return new String[] {folderPath, fileName};
        }
        return null;

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


    public void updateProject(Long id, ProjectUpdateRequestDto requestDto, String memberId) {

        Project project = projectRepository.findById(id).orElseThrow();

        if(project.getMember() != null && project.getMember().getId().equals(Long.parseLong(memberId))){

            project.updateProject(requestDto);
        }
        projectRepository.save(project);

    }

    public int likeProject(Long id, String memberId) {

        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow(NotFoundMemberException::new);
        Project project = projectRepository.findById(id).orElseThrow(NotFoundProjectException::new);

        ProjectLike projectLike = projectLikeRepository.findByProjectAndMember(project, member);

        if(projectLike != null){
            projectLikeRepository.delete(projectLike);
            project.likeCntDown();

            projectRepository.save(project);
            return 0;
        } else {

            projectLikeRepository.save(
                    ProjectLike.builder()
                            .project(project)
                            .member(member)
                            .build());

            project.likeCntUp();
            projectRepository.save(project);

            return 1;
        }
    }
}
