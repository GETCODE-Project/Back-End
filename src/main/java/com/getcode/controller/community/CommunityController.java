package com.getcode.controller.community;

import com.getcode.dto.community.requset.CommunityCommentRequestDto;
import com.getcode.dto.community.requset.CommunityRequestDto;
import com.getcode.dto.community.response.CommunityCommentResponseDto;
import com.getcode.dto.community.response.CommunityDetailResponseDto;
import com.getcode.dto.community.response.CommunityInfoResponseDto;
import com.getcode.dto.community.response.CreatedCommunityResponseDto;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커뮤니티 게시판 관련 API 명세")
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
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.createCommunity(req));
    }
    @Operation(summary = "모든 스터디 모집글 전체 조회", description = "pageNumber 필수 => 10개씩 반환"
            + "criteria => default: modifiedDate(최신순) / views(조회수), count(좋아요))"
            + "나머지는 필수는 아니고 있으면 필터링해서 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search/communities")
    public ResponseEntity<List<CommunityInfoResponseDto>> findAllStudy(
            @Parameter(description = "검색어")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "커뮤니티 유형")
            @RequestParam(value = "", required = false) String category,
            @Parameter(description = "정렬 기준: latestOrder, pastOrder, likeCnt중 하나여야 합니다.")
            @RequestParam(defaultValue = "latestOrder", required = false) String sort,
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page") int page,
            @Parameter(description = "한 페이지의 아이템 갯수")
            @RequestParam(value = "size") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(communityService.searchCommunity(keyword,category,sort,page,size));
    }

    @Operation(summary = "로그인한 사용자가 작성한 게시글 목록 조회", description = "특정 사용자가 작성한 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/communities")
    public ResponseEntity<List<CommunityInfoResponseDto>> findAllCommunityByMember() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(communityService.findAllCommunityByMember());
    }

    @Operation(summary = "게시글 수정", description = "PathVariable, 게시글 변경내용을 입력받아 게시글 수정(제목, 내용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/community/{id}")
    public ResponseEntity<String> editCommunity(@PathVariable(name = "id") Long id,
                                                                    @RequestBody CommunityRequestDto req) {
        communityService.editCommunity(id,req);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 수정 성공");
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
    public ResponseEntity<CommunityDetailResponseDto> findCommunity(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(communityService.findCommunity(id));
    }

    @Operation(description = "게시글 좋아요")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/community-like/{id}")
    public ResponseEntity<String> likeCommunity(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.likeCommunity(id));
    }

    @Operation(description = "게시글 찜하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/community-wish/{id}")
    public ResponseEntity<String> wishCommunity(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(communityService.wishCommunity(id));
    }

    @Operation(summary = "커뮤니티 게시글에 댓글", description = "게시글 Id를 입력받아 해당 게시글을 찾은 후 댓글")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/community/comment/{id}")
    public ResponseEntity<String> addComment(@PathVariable(name = "id") Long id,
                                             @RequestBody CommunityCommentRequestDto req) {
        communityService.addComment(req, id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 등록 성공");
    }

    @Operation(summary = "커뮤니티 게시글 댓글 수정", description = "댓글 Id를 입력받아 댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/community/comment/{id}")
    public ResponseEntity<String> editComment(@PathVariable(name = "id") Long id,
                                                                   @RequestBody CommunityCommentRequestDto req) {
        communityService.editComment(req, id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 성공");
    }

    @Operation(summary = "커뮤니티 게시글 댓글 삭제", description = "댓긋 Id를 입력받아 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @DeleteMapping("/community/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "id") Long id) {
        communityService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }

}
