package com.getcode.controller.mypage;

import com.getcode.config.security.SecurityUtil;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.service.mypage.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "마이페이지 관련 api명세")
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "본인이 작성한 프로젝트 조회")
    @GetMapping("/my")
    ResponseEntity<?> getMyProject(){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectInfoResponseDto> myProject = myPageService.getMyProject(memberEmail);

        return ResponseEntity.status(HttpStatus.OK).body(myProject);
    }


    @Operation(summary = "본인이 찜한 프로젝트 조회")
    @GetMapping("/my")
    ResponseEntity<?> getMyWishProject(){

        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        List<ProjectInfoResponseDto> myProject = myPageService.getMyWishProject(memberEmail);

        return ResponseEntity.status(HttpStatus.OK).body(myProject);
    }

}
