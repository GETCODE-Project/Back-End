package com.getcode.controller.community;

import com.getcode.dto.community.CommunityRequestDto;
import com.getcode.dto.community.CreatedCommunityResponseDto;
import com.getcode.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판 관련 API 명세")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @Operation(summary = "게시판 작성",
            description = "제목: 2자 이상 / 내용: 2자 이상 / 게시판 종류: qna(질문), counsel(고민상담), freedom(자유게시판)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/community")
    public ResponseEntity<CreatedCommunityResponseDto> createStudy(@Valid @RequestBody CommunityRequestDto req) {
        CreatedCommunityResponseDto res = communityService.createCommunity(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
