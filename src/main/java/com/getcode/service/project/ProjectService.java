package com.getcode.service.project;

import com.getcode.config.s3.S3Service;
import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.*;
import com.getcode.dto.project.ProjectSpecification;
import com.getcode.dto.project.req.*;
import com.getcode.dto.project.res.*;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.*;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.project.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStackRepository projectStackRepository;
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
                    /*
                    List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(id);
                    deleteS3File(projectImages);
                     */
                    projectRepository.deleteById(id);
                    return 1; // 삭제 성공
                })
                .orElseThrow(() -> new NotFoundProjectException()); // 해당 id에 해당하는 프로젝트가 없음

    }


    //s3 파일삭제 메소드
    private void deleteS3File(List<ProjectImage> projectImages){
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
    public void addProject(ProjectRequestDto projectRequestDto, String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Project project = projectRequestDto.toProjectEntity(member);

        projectRepository.save(project);
/*
        if(fileType != null && !multipartFiles.isEmpty() && multipartFiles != null) {

            List<String> fileUrls = uploadS3File(multipartFiles, fileType);

            projectRequestDto.setImageUrls(fileUrls);

            List<String> imageList = projectRequestDto.getImageUrls();

            for(String imageUrl : imageList){
                projectImageRepository.save(ProjectImageDto.toEntity(project, imageUrl));
            }

        }
*/

        List<String> techList = projectRequestDto.getTechStacks();


        for(String techStack : techList){
            projectStackRepository.save(ProjectTechDto.toEntity(project, techStack));
        }



    }

    //깃헙 url 중복체크
    @Transactional(readOnly = true)
    public Boolean checkGithubUrlDuplication(String githubUrl) {
        Boolean urlDuplicate = projectRepository.existsByGithubUrl(githubUrl);
        return urlDuplicate;
    }


    //프로젝트 수정
    @Transactional
    public void updateProject(Long id, ProjectUpdateRequestDto requestDto, String memberEmail) {

        Project project = projectRepository.findById(id).orElseThrow(NotFoundCommentException::new);

        if (project.getMember() != null && project.getMember().getEmail().equals(memberEmail)) {
            /*
            //새로운 파일 추가했을 때 기존 파일 삭제 후 새로운 파일 등록
            if(fileType != null && !multipartFiles.isEmpty()) {
                List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(id);

                deleteS3File(projectImages);

                //확장성을 고려하여 List형태로 파일 저장
                List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
                //파일 url리스트로 변환
                List<String> fileUrls = files.stream()
                        .map(S3FileDto::getUploadFileUrl)
                        .collect(Collectors.toList());

                requestDto.setImageUrls(fileUrls);
            */
            project.updateProject(requestDto);
        } else {
            throw new NotMatchMemberException();
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


        String owner = project.getMember().getEmail();

        if(owner == memberEmail){
            throw new NotOwnWishException();
        }


        if(wishProject != null){
            projectWishRepository.delete(wishProject);
            projectRepository.save(project);
            return 0;
        } else {
            projectWishRepository.save(
                    WishProject.builder()
                            .project(project)
                            .member(member)
                            .build());
            projectRepository.save(project);
            return 1;
        }



    }

    //프로젝트 상세조회
    @Transactional
    public ProjectDetailResponseDto getProject(Long id) {

        Project project = projectRepository.findById(id).orElseThrow(NotFoundProjectException::new);

        List<ProjectLike> projectLike = projectLikeRepository.findByProject(project);
        List<WishProject> wishProject = projectWishRepository.findByProject(project);

        Boolean checkLike = false;
        Boolean checkWish = false;
        Boolean checkWriter = false;

        if(SecurityUtil.getCurrentMemberEmail() != null) {
            String memberEmail = SecurityUtil.getCurrentMemberEmail();

            for (ProjectLike checkLiked : projectLike) {
                if(checkLiked.getMember().getEmail().equals(memberEmail)){
                    checkLike = true;
                    break;
                }
            }
            for (WishProject checkWished : wishProject) {
                if(checkWished.getMember().getEmail().equals(memberEmail)){
                    checkWish = true;
                    break;
                }
            }

            if (project.getMember().getEmail().equals(memberEmail)){
                checkWriter = true;
            }

        }

        project.viewCntUp();

        ProjectDetailResponseDto responseDto = new ProjectDetailResponseDto(projectRepository.save(project), checkLike, checkWish, checkWriter);


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
    @Transactional
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
    @Transactional
    public int updateComment(Long id, Long projectId, String memberEmail, CommentUpdateRequestDto requestDto) {

        ProjectComment projectComment = projectCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);

        if(projectComment.getProject().getId().equals(projectId) && projectComment.getMember().getEmail().equals(memberEmail)){

            projectComment.updateComment(requestDto);
            projectCommentRepository.save(projectComment);
            return 1;
        } else return -1;


    }

    //전체 프로젝트 조회(조건 검색) 조건: 주제, 기술스택, 년도 | 정렬 조건: 최신순, 과거순, 좋아요순
    @Transactional
    public List<ProjectInfoResponseDto> getProjectList(int size, int pageNumber, String sort, String keyword,
                                                       String subject, List<String> techStack,
                                                       Integer year) {

        Sort sortCriteria;

            Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseGet(() -> null);


        if(sort.equals("pastOrder")){
            sortCriteria = Sort.by(Sort.Direction.ASC, "modifiedDate");
        } else if (sort.equals("likeCnt")) {
            sortCriteria = Sort.by(Sort.Direction.DESC, "likeCnt");
        } else {
            sortCriteria = Sort.by(Sort.Direction.DESC, "modifiedDate");
        }

        Pageable pageable = PageRequest.of(pageNumber -1, size, sortCriteria);


        List<Specification<Project>> specifications = new ArrayList<>();

        if (techStack != null && !techStack.isEmpty()) {
            specifications.add(ProjectSpecification.techStackLike(techStack));
        }

        if (subject != null && !subject.isEmpty()) {
            specifications.add(ProjectSpecification.subjectLike(subject));
        }

        if (year != null) {
            specifications.add(ProjectSpecification.yearBetween(year));
        }

        if (keyword != null && !keyword.isEmpty()) {
            specifications.add(ProjectSpecification.keywordLikeTitleOrContentOrIntroduction(keyword));
        }


        Specification<Project> combinedSpec = ProjectSpecification.combineSpecifications(specifications);

        Page<Project> projectPage = projectRepository.findAll(combinedSpec, pageable);

        List<ProjectInfoResponseDto> responseDto = new ArrayList<>();

        projectPage.forEach((project) ->{

            ProjectInfoResponseDto dto = new ProjectInfoResponseDto(project);
            //로그인 하지 않았을 때
            if(member != null) {
                dto.setCheckLike(isProjectLikedByUser(project.getId(), member.getId()));
                dto.setCheckWish(isProjectWishedByUser(project.getId(), member.getId()));
            } else{
                dto.setCheckLike(Boolean.FALSE);
                dto.setCheckWish(Boolean.FALSE);
            }
                responseDto.add(dto);
                // 좋아요 및 찜 정보 가져오기

        });

        return responseDto;

    }
    //조회한 유저가 좋아요 한 게시글 검사
    public Boolean isProjectLikedByUser(Long projectId, Long memberId) {
        return projectLikeRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }
    //조회한 유저가 찜 한 게시글 검사
    public Boolean isProjectWishedByUser(Long projectId, Long memberId) {
        return projectWishRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }


    //특정 프로젝트 댓글 정보 List return
    @Transactional
    public List<CommentResponseDto> getProjectComment(Long projectId) {

        List<ProjectComment> projectComments = projectCommentRepository.findByProjectId(projectId);
        List<CommentResponseDto> responseDto = new ArrayList<>();

        for(ProjectComment projectComment : projectComments){
            responseDto.add(new CommentResponseDto(projectComment));
        }
        return responseDto;

    }

    public List<String> uploadS3File(List<MultipartFile> multipartFiles, String fileType){
        //확장성을 고려하여 List형태로 파일 저장
        List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
        //파일 url리스트로 변환
        List<String> fileUrls = files.stream()
                .map(S3FileDto::getUploadFileUrl)
                .collect(Collectors.toList());
        return fileUrls;
    }

}