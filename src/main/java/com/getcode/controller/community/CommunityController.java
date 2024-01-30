package com.getcode.controller.community;

import com.getcode.dto.community.*;
import com.getcode.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CreatedCommunityResponseDto> createCommunity(@Valid @RequestBody CommunityRequestDto req) {
        CreatedCommunityResponseDto res = communityService.createCommunity(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "로그인한 사용자가 작성한 게시글 목록 조회", description = "특정 사용자가 작성한 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityResponseDto>> findAllCommunityByMember() {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findAllCommunityByMember());
    };;;

    @Operation(summary = "게시글 수정", description = "PathVariable, 게시글 변경내용을 입력받아 게시글 수정(제목, 내용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/community/{id}")
    public ResponseEntity<CommunityResponseDto> editCommunity(@PathVariable(name = "id") Long id,
                                                              @RequestBody CommunityEditDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.editCommunity(id, req));
    }

    @Operation(summary = "특정 게시글 삭제", description = "PathVariable 입력 후, 작성한 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/community/{id}")
    public void deleteCommunity(@PathVariable(name = "id") Long id) {
        communityService.deleteCommunity(id);
    }

    @Operation(summary = "특정 게시글 조회", description = "PathVariable을 입력받아 게시글 조회후 조회수 1 증가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/community/{id}")
    public ResponseEntity<CommunityResponseDto> findCommunity(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findCommunity(id));
    }

    @Operation(summary = "게시글 좋아요", description = "좋아요를 하면 전체 게시글 정보 리턴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/community-like/{id}")
    public ResponseEntity<CommunityResponseDto> likeCommunity(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.likeCommunity(id));
    }

    @Operation(summary = "커뮤니티 게시글에 댓글", description = "게시글 Id를 입력받아 해당 게시글을 찾은 후 댓글")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/community/comment/{id}")
    public ResponseEntity<CommunityCommentResponseDto> addComment(@PathVariable(name = "id") Long id,
                                                                  @RequestBody CommunityCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.addComment(req, id));
    }

    @Operation(summary = "커뮤니티 게시글 댓글 수정", description = "댓글 Id를 입력받아 댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/community/comment/{id}")
    public ResponseEntity<CommunityCommentResponseDto> editComment(@PathVariable(name = "id") Long id,
                                                               @RequestBody CommunityCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.editComment(req, id));
    }

    @Operation(summary = "커뮤니티 게시글 댓글 삭제", description = "댓긋 Id를 입력받아 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @DeleteMapping("/community/comment/{id}")
    public void deleteComment(@PathVariable(name = "id") Long id) {
        communityService.deleteComment(id);
    }

}
