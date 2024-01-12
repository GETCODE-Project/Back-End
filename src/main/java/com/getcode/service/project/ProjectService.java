package com.getcode.service.project;

import com.getcode.config.s3.S3Service;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.*;
import com.getcode.dto.project.ProjectSpecification;
import com.getcode.dto.project.req.CommentRequestDto;
import com.getcode.dto.project.req.CommentUpdateRequestDto;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import com.getcode.dto.project.res.ProjectDetailResponseDto;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.NotFoundProjectException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.project.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectSubjectRepository projectSubjectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final MemberRepository memberRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final ProjectWishRepository projectWishRepository;
    private final S3Service s3Service;

    //프로젝트 삭제
    @Transactional
    public int deleteProject(Long id, String memberEmail) {

        Optional<Project> existedProject = projectRepository.findById(id);

        return existedProject
                .filter(project -> project.getMember() != null && project.getMember().getEmail().equals(memberEmail)) //로그인한 유저와 글 쓴 유저 일치하는지 확인
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


    //프로젝트 등록
    @Transactional
    public void insertProject(ProjectRequestDto projectRequestDto, String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

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

    //깃헙 url 중복체크
    @Transactional(readOnly = true)
    public Boolean checkGithubUrlDuplication(String githubUrl) {
        Boolean urlDuplicate = projectRepository.existsByGithubUrl(githubUrl);
        return urlDuplicate;
    }


    //프로젝트 수정 진행중ing
    @Transactional
    public void updateProject(Long id, ProjectUpdateRequestDto requestDto, String memberEmail, String fileType, List<MultipartFile> multipartFiles) {

        Project project = projectRepository.findById(id).orElseThrow();

        if(project.getMember() != null && project.getMember().getEmail().equals(memberEmail)){

            if(fileType != null && !multipartFiles.isEmpty()) {
                List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(id);
                List<String> imageUrls = new ArrayList<>();

                for (ProjectImage projectImage : projectImages) {
                    String url = projectImage.getImageUrl();
                    imageUrls.add(url);
                }

                //변환한 url S3에서 삭제
                for (String imageUrl : imageUrls) {

                    String[] files = extractPathFromImageUrl(imageUrl);

                    String result = s3Service.deleteFile(files[0], files[1]);

                }




                //확장성을 고려하여 List형태로 파일 저장
                List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
                //파일 url리스트로 변환
                List<ProjectImage> fileUrls = files.stream()
                        .map(S3FileDto::getUploadFileUrl)
                        .map(url -> ProjectImage.builder().imageUrl(url).build())
                        .collect(Collectors.toList());

                requestDto.setImageUrls(fileUrls);

                List<ProjectImage> projectImageUrls = requestDto.getImageUrls();
                List<ProjectTech> techStacks = requestDto.getTechStackList();
                List<ProjectSubject> projectSubjects = requestDto.getProjectSubjects();

                for(ProjectImage projectImage : projectImageUrls){
                    project.projectImageAdd(projectImage.getImage(projectImage.getImageUrl(), project));
                }
                for(ProjectSubject projectSubject : projectSubjects){
                    project.projectSubjectAdd(projectSubject.getSubject(projectSubject.getSubject(), project));
                }
                for(ProjectTech projectTech : techStacks){
                    project.stackAdd(projectTech.getTechStack(projectTech.getTechStack(), project));
                }
/*
                    Iterator<ProjectImage> imageIterator = projectImageUrls.iterator();
                    while (imageIterator.hasNext()) {
                        ProjectImage projectImage = imageIterator.next();
                        project.projectImageAdd(projectImage.getImage(projectImage.getImageUrl(), project));
                    }
*/
            }
            project.updateProject(requestDto);

            projectRepository.save(project);

        } else {
            throw new NotFoundMemberException();
        }

    }



    //프로젝트 좋아요
    @Transactional
    public int likeProject(Long id, String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);
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

    //프로젝트 즐겨찾기
    @Transactional
    public int wishProject(Long id, String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);
        Project project = projectRepository.findById(id).orElseThrow(NotFoundProjectException::new);

        WishProject wishProject = projectWishRepository.findByProjectAndMember(project, member);

        if(wishProject != null){
            projectWishRepository.delete(wishProject);
            project.wishCntDown();
            projectRepository.save(project);
            return 0;
        } else {
            projectWishRepository.save(
                    WishProject.builder()
                            .project(project)
                            .member(member)
                            .build());
            project.wishCntUp();
            projectRepository.save(project);
            return 1;
        }



    }


    //프로젝트 상세조회
    public ProjectDetailResponseDto getProject(Long id) {

        Project project = projectRepository.findById(id).orElseThrow(NotFoundProjectException::new);
        project.viewCntUp();
        ProjectDetailResponseDto responseDto = new ProjectDetailResponseDto(projectRepository.save(project));

        return responseDto;

    }



    //프로젝트 댓글 등록
    @Transactional
    public void addComment(Long id, String memberEmail, CommentRequestDto requestDto) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);
        Project project = projectRepository.findById(id).orElseThrow(NotFoundProjectException::new);

        projectCommentRepository.save(requestDto.toEntity(project, member));

    }

    //프로젝트 댓글 삭제
    public int deleteComment(Long id, Long projectId, String memberEmail) {

        Optional<ProjectComment> projectComment = projectCommentRepository.findById(id);

        return projectComment
                .filter(comment -> comment.getProject().getId().equals(projectId) && comment.getMember().getEmail().equals(memberEmail))
                .map(comment -> {
                    projectCommentRepository.deleteById(id);
                    return 1;
                })
                .orElseGet(() -> -1);
    }

    //프로젝트 댓글 수정
    public int updateComment(Long id, Long projectId, String memberEmail, CommentUpdateRequestDto requestDto) {

        ProjectComment projectComment = projectCommentRepository.findById(id).orElseThrow();


        if(projectComment.getProject().getId().equals(projectId) && projectComment.getMember().getEmail().equals(memberEmail)){

                   projectComment.updateComment(requestDto);
                   projectCommentRepository.save(projectComment);
                   return 1;
        } else return -1;


    }

    //전체 프로젝트 조회(조건 검색) 조건: 주제, 기술스택, 년도 | 정렬 조건: 최신순, 과거순, 좋아요순
    public Slice<Project> getProjectList(int size, int page, String sort, String keyword, List<String> subject, List<String> techStack, String year) {

        Sort sortCriteria;

        if(sort.equals("pastOrder")){
            sortCriteria = Sort.by(Sort.Direction.ASC, "createDate");
        } else if (sort.equals("likeCnt")) {
            sortCriteria = Sort.by(Sort.Direction.DESC, "likeCnt");
        } else {
            sortCriteria = Sort.by(Sort.Direction.DESC, "createDate");
        }

        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        Slice<Project> projectPage = null;

        List<ProjectTech> projectTeches = null;
        if(techStack != null && !techStack.isEmpty()){
            projectTeches = techStack.stream()
                                    .map(ProjectTech::new)
                                    .collect(Collectors.toList());
        }

        List<ProjectSubject> projectSubject = null;
        if(subject != null && !subject.isEmpty()){
            projectSubject = subject.stream()
                                    .map(ProjectSubject::new)
                                    .collect(Collectors.toList());
        }


        List<Specification<Project>> specifications = new ArrayList<>();

        if (!techStack.isEmpty() && techStack != null) {
            specifications.add(ProjectSpecification.techStackLike(projectTeches));
        }

        if (!subject.isEmpty() && subject != null) {
            specifications.add(ProjectSpecification.subjectLike(projectSubject));
        }

        if (year != null) {
            specifications.add(ProjectSpecification.yearBetween(year));
        }

        if (keyword != null && !keyword.equals(" ")) {
            specifications.add(ProjectSpecification.keywordLikeTitle(keyword));
            specifications.add(ProjectSpecification.keywordLikeContent(keyword));
        }

        Specification<Project> combinedSpec = ProjectSpecification.combineSpecifications(specifications);

        return projectPage = projectRepository.findAll(combinedSpec, pageable);


    }



}
