package com.getcode.controller.project;
import com.getcode.config.s3.S3Service;
import com.getcode.config.security.SecurityUtil;
import com.getcode.dto.project.req.CommentRequestDto;
import com.getcode.dto.project.req.CommentUpdateRequestDto;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.project.req.ProjectUpdateRequestDto;
import com.getcode.dto.project.res.CommentResponseDto;
import com.getcode.dto.project.res.ProjectDetailResponseDto;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "프로젝트 관련 기능 api명세")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final S3Service s3Service;


    @Operation(summary = "프로젝트 정보 등록 api")
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@Parameter(description = "프로젝트 등록 값")
                                        @Valid @RequestBody ProjectRequestDto projectRequestDto
                                        //@Parameter(description = "프로젝트 이미지")
                                        //@RequestParam(name = "fileType", required = false) String fileType,
                                        //@RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles
    ){




        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        projectService.addProject(projectRequestDto, memberEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body("등록이 완료되었습니다.");

    }


    @Operation(summary = "github url 중복확인 api")
    @PostMapping("/add/checkUrl")
    public ResponseEntity<?> checkUrl(@Parameter(description = "github Url") @RequestParam String githubUrl) {

        Boolean result = projectService.checkGithubUrlDuplication(githubUrl);
        //result: false - 사용가능 , true: 중복
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("중복된 Url입니다.");
        } else{
            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 Url입니다.");
        }

    }


    @Operation(summary = "프로젝트 삭제 api")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProject(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int res = 0;

        res = projectService.deleteProject(id, memberEmail);

        if(res <= 0 ){
            return ResponseEntity.status(HttpStatus.OK).body("삭제 실패.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("삭제가 완료되었습니다.");
    }



    @Operation(summary = "프로젝트 수정 api")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateProject(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id,
                                           @RequestBody(required = false) ProjectUpdateRequestDto requestDto
                                           //@RequestPart(name = "fileType", required = false) String fileType,
                                           //@RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles
    ){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        projectService.updateProject(id, requestDto,memberEmail);

        return ResponseEntity.status(HttpStatus.OK).body("수정 완료.");
    }




    @Operation(summary = "프로젝트 좋아요 api")
    @PostMapping("/{id}/like")
    ResponseEntity<?> likeProject(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.likeProject(id, memberEmail);
        Boolean checkLike = false;
        if(result == 1) {
            checkLike = true;
            return ResponseEntity.status(HttpStatus.OK).body("프로젝트 좋아요 성공."+ "\n"+ "checkLike: "+checkLike);
        } else if (result == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("프로젝트 좋아요 삭제 성공."+ "\n"+ "checkLike: "+checkLike);
        }
        return ResponseEntity.status(HttpStatus.OK).body("프로젝트 좋아요 등록 또는 삭제 실패");
    }


    @Operation(summary = "프로젝트 찜 api")
    @PostMapping("/{id}/wish")
    ResponseEntity<?> wishProject(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.wishProject(id, memberEmail);
        Boolean checkWish = false;

        if(result == 1){
            checkWish = true;
            return ResponseEntity.status(HttpStatus.OK).body("프로젝트 찜 성공."+ "\n"+ "checkWish: "+checkWish);
        } else if (result == 0){
            return ResponseEntity.status(HttpStatus.OK).body("프로젝트 찜 삭제 성공." + "\n" + "checkWish: " +checkWish);
        }
        return ResponseEntity.status(HttpStatus.OK).body("프로젝트 찜 등록 또는 삭제 실패");
    }



    @Operation(summary = "특정 프로젝트 상세정보 조회 api")
    @GetMapping("/detail/{id}")
    ResponseEntity<?> getProject(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id){

        ProjectDetailResponseDto responseDto = projectService.getProject(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }




    @Operation(summary = "전체 프로젝트 조회 api")
    @GetMapping("/all")
    ResponseEntity<?> getProjectList(@Parameter(description = "정렬 기준: latestOrder, pastOrder, likeCnt중 하나여야 합니다.")
                                     @RequestParam(defaultValue = "latestOrder", required = false) String sort,
                                     @Parameter(description = "페이지 수")
                                     @Min(value = 0, message = "page값은 0이상이어야 합니다")
                                     @RequestParam(defaultValue = "0") int pageNumber,
                                     @Parameter(description = "한 페이지에 담기는 개수")
                                     @Positive(message = "size값은 1이상이어야 합니다")
                                     @RequestParam(defaultValue = "10") int size,
                                     @Parameter(description = "검색어") @RequestParam(defaultValue = "", required = false) String keyword,
                                     @Parameter(description = "주제") @RequestParam(defaultValue = "", required = false) String subject,
                                     @Parameter(description = "기술스택") @RequestParam(defaultValue = "", required = false) List<String> techStack,
                                     @Parameter(description = "년도") @RequestParam(defaultValue = "2024", required = false) Integer year
    )
    {

        List<ProjectInfoResponseDto> projectLists = projectService.getProjectList(size, pageNumber, sort, keyword, subject, techStack, year);

        return ResponseEntity.status(HttpStatus.OK).body(projectLists);

    }





    @Operation(summary = "프로젝트 댓글 등록 api")
    @PostMapping("/detail/{id}/comment/add")
    ResponseEntity<?> addComment(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id,
                                 @RequestBody CommentRequestDto requestDto)
    {


        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        projectService.addComment(id, memberEmail, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 등록이 완료되었습니다.");

    }


    @Operation(summary = "프로젝트 댓글 삭제 api")
    @DeleteMapping("/detail/{projectId}/comment/delete/{id}")
    ResponseEntity<?> deleteComment(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "projectId") Long projectId,
                                    @Parameter(description = "댓글 아이디") @PathVariable(name = "id") Long id)
    {

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.deleteComment(id, projectId, memberEmail);

        if(result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 실패");
        }

    }


    @Operation(summary = "프로젝트 댓글 수정 api")
    @PutMapping("/detail/{projectId}/comment/update/{id}")
    ResponseEntity<?> updateComment(@Parameter(description = "프로젝트 아이디") @PathVariable(name = "projectId") Long projectId,
                                    @Parameter(description = "프로젝트 아이디") @PathVariable(name = "id") Long id,
                                    @RequestBody CommentUpdateRequestDto requestDto)
    {
        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        int result = projectService.updateComment(id, projectId, memberEmail, requestDto);

        if (result == 1){
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 실패");
        }


    }



    @Operation(summary = "특정 프로젝트 게시글 댓글정보 조회")
    @GetMapping("/{projectId}/comment")
    ResponseEntity<?> getProjectComment(@Parameter(description = "프로젝트 아이디")@PathVariable(name = "projectId") Long projectId){

        List<CommentResponseDto> responseDto = projectService.getProjectComment(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }




}