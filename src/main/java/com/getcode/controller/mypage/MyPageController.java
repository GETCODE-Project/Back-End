package com.getcode.controller.mypage;

import com.getcode.config.security.SecurityUtil;
import com.getcode.dto.community.response.CommunityInfoResponseDto;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentInfoResDto;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.service.mypage.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "마이페이지 관련 api명세")
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "본인이 작성한 프로젝트 조회")
    @GetMapping("/project")
    ResponseEntity<?> getMyProject(@Parameter(description = "페이지 수") @RequestParam int page,
                                   @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectInfoResponseDto> myProject = myPageService.getMyProject(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myProject);
    }


    @Operation(summary = "본인이 찜한 프로젝트 조회")
    @GetMapping("/project/wish")
    ResponseEntity<?> getMyWishProject(@Parameter(description = "페이지 수") @RequestParam int page,
                                       @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectInfoResponseDto> myProject = myPageService.getMyWishProject(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myProject);
    }

    @Operation(summary = "본인이 작성한 프로젝트 모집 조회")
    @GetMapping("/recruit")
    ResponseEntity<?> getMyRecruitment(@Parameter(description = "페이지 수") @RequestParam int page,
                                       @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectRecruitmentInfoResDto> myRecruitment = myPageService.getMyRecruitment(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myRecruitment);
    }


    @Operation(summary = "본인이 찜한 프로젝트 모집 조회")
    @GetMapping("/recruit/wish")
    ResponseEntity<?> getMyWishRecruitment(@Parameter(description = "페이지 수") @RequestParam int page,
                                       @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectRecruitmentInfoResDto> myWishRecruitment = myPageService.getMyWishRecruitment(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myWishRecruitment);
    }

    @Operation(summary = "본인이 작성한 스터디 모집글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/study")
    public ResponseEntity<List<StudyInfoResponseDto>> findAllStudyByMember(@Parameter(description = "페이지 수") @RequestParam int page,
                                                                           @Parameter(description = "객체 수") @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(myPageService.findAllStudyByMember(page, size));
    }


    @Operation(summary = "본인이 찜한 스터디 모집글 조회")
    @GetMapping("/study/wish")
    ResponseEntity<?> getMyWishStudy(@Parameter(description = "페이지 수") @RequestParam int page,
                                     @Parameter(description = "객체 수") @RequestParam int size){
        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        List<StudyInfoResponseDto> myWishStudy = myPageService.getMyWishStudy(memberEmail, size, page);
        return ResponseEntity.status(HttpStatus.OK).body(myWishStudy);
    }


    @Operation(summary = "본인이 작성한 커뮤니티 글 조회")
    @GetMapping("/community")
    ResponseEntity<?> getMyCommunity(@Parameter(description = "페이지 수") @RequestParam int page,
                                   @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<CommunityInfoResponseDto> myCommunity = myPageService.getMyCommunity(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myCommunity);
    }


    @Operation(summary = "본인이 찜한 프로젝트 조회")
    @GetMapping("/community/wish")
    ResponseEntity<?> getMyWishCommunity(@Parameter(description = "페이지 수") @RequestParam int page,
                                       @Parameter(description = "객체 수") @RequestParam int size){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<CommunityInfoResponseDto> myWishCommunity = myPageService.getMyWishCommunity(memberEmail, size, page);

        return ResponseEntity.status(HttpStatus.OK).body(myWishCommunity);
    }




}
