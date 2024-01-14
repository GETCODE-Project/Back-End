package com.getcode.controller.community;

import com.getcode.domain.community.Community;
import com.getcode.dto.community.CommunityEditDto;
import com.getcode.dto.community.CommunityRequestDto;
import com.getcode.dto.community.CommunityResponseDto;
import com.getcode.dto.community.CreatedCommunityResponseDto;
import com.getcode.dto.study.StudyEditDto;
import com.getcode.dto.study.StudyInfoResponseDto;
import com.getcode.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            description = "제목: 2자 이상 / 내용: 2자 이상 / 게시판 종류: qna(질문), counsel(고민상담), free(자유게시판)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/community")
    public ResponseEntity<CreatedCommunityResponseDto> createStudy(@Valid @RequestBody CommunityRequestDto req) {
        CreatedCommunityResponseDto res = communityService.createCommunity(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "로그인한 사용자가 작성한 게시글 전체 조회", description = "특정 사용자가 작성한 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityResponseDto>> findAllCommunityByMember() {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAllCommunityByMember());
    }

    @Operation(summary = "게시글 수정", description = "PathVariable, 게시글 변경내용을 입력받아 게시글 수정(제목, 내용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/community/{id}")
    public ResponseEntity<CommunityResponseDto> editCommunity(@PathVariable(name = "id") Long id,
                                                              @RequestBody CommunityEditDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.editCommunity(id, req));
    }
}
