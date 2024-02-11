package com.getcode.controller.study;

import com.getcode.dto.study.response.StudyCommentResponseDto;
import com.getcode.dto.study.response.StudyDetailResponseDto;
import com.getcode.dto.study.response.StudyInfoResponseDto;
import com.getcode.dto.study.request.StudyCommentRequestDto;
import com.getcode.dto.study.request.StudyRequestDto;
import com.getcode.dto.study.response.CreatedStudyResponseDto;
import com.getcode.service.study.StudyService;
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

@Tag(name = "스터디 관련 API 명세")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 모집글 작성",
            description = "제목: 2자 이상 / 내용: 2자 이상 / 지역: 필수 / 온라인 여부: 필수 / 주제: 리스트 형식"
                    + "연락 방법: 작성, ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED")
    })
    @PostMapping("/study")
    public ResponseEntity<CreatedStudyResponseDto> createStudy(@Valid @RequestBody StudyRequestDto req) {
        CreatedStudyResponseDto study = studyService.createStudy(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(study);
    }

    @Operation(summary = "스터디 모집글 조회", description = "PathVariable을 입력받아 게시글 조회후 조회수 1 증가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/study/{id}")
    public ResponseEntity<StudyDetailResponseDto> findStudy(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.findStudy(id));
    }

    @Operation(summary = "모든 스터디 모집글 전체 조회", description = "pageNumber 필수 => 10개씩 반환"
            + "criteria => default: modifiedDate(최신순) / views(조회수), count(좋아요))"
            + "나머지는 필수는 아니고 있으면 필터링해서 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search/studies")
    public ResponseEntity<List<StudyInfoResponseDto>> findAllStudy(
            @Parameter(description = "검색어")
            @RequestParam(value = "keyword") String keyword,
            @Parameter(description = "지역-시,도")
            @RequestParam(value = "siDo") String siDo,
            @Parameter(description = "지역-구,군")
            @RequestParam(value = "guGun") String guGun,
            @Parameter(description = "모집여부(모집중 true, 모집 완료 false)")
            @RequestParam(value = "recruitment") Boolean recruitment,
            @Parameter(description = "스터디 유형(온라인 스터디 true, 오프라인 스터디 false)")
            @RequestParam(value = "online") Boolean online,
            @Parameter(description = "연도")
            @RequestParam(value = "year") Integer year,
            @Parameter(description = "스터디 분야")
            @RequestParam(value = "field") List<String> fields,
            @Parameter(description = "정렬 기준: latestOrder, pastOrder, likeCnt중 하나여야 합니다.")
            @RequestParam(defaultValue = "latestOrder") String sort,
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page") int page,
            @Parameter(description = "한 페이지의 아이템 갯수")
            @RequestParam(value = "size") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.searchStudy(
                keyword, siDo, guGun, recruitment, online, year, fields, page, size, sort));
    }

    @Operation(summary = "스터디 게시글에 댓글", description = "스터디 Id를 입력받아 해당 스터디를 찾은 후 댓글")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/study/comment/{id}")
    public ResponseEntity<StudyCommentResponseDto> addComment(@PathVariable(name = "id") Long id,
                                                              @RequestBody StudyCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.addComment(req, id));
    }

    @Operation(summary = "스터디 게시글 댓글 수정", description = "댓글 Id를 입력받아 댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/study/comment/{id}")
    public ResponseEntity<StudyCommentResponseDto> editComment(@PathVariable(name = "id") Long id,
                                                              @RequestBody StudyCommentRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.editComment(req, id));
    }

    @Operation(summary = "스터디 게시글 댓글 삭제", description = "댓글 Id를 입력받아 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @DeleteMapping("/study/comment/{id}")
    public void deleteComment(@PathVariable(name = "id") Long id) {
        studyService.deleteComment(id);
    }

    @Operation(summary = "스터디 좋아요", description = "좋아요를 하면 전체 스터디 정보 리턴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/study-like/{id}")
    public ResponseEntity<String> addComment(@PathVariable(name = "id") Long id){
        studyService.likeStudy(id);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요 완료");
    }

    @Operation(summary = "스터디 모집글 수정", description = "PathVariable, 스터디 변경내용을 입력받아 스터디 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/study/{id}")
    public ResponseEntity<StudyInfoResponseDto> editStudy(@PathVariable(name = "id") Long id,
                                                      @RequestBody StudyRequestDto req) {
        return ResponseEntity.status(HttpStatus.OK).body(studyService.editStudy(id, req));
    }

    @Operation(summary = "스터디 모집글 삭제", description = "PathVariable 입력 후, 작성한 스터디 모집글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/study/{id}")
    public void deleteStudy(@PathVariable(name = "id") Long id) {
        studyService.deleteStudy(id);
    }

    @Operation(summary = "스터디 찜", description = "PathVariable 입력 후, 스터디 찜하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/study-wish/{id}")
    public void wishStudy(@PathVariable(name = "id") Long id) {
        studyService.wishStudy(id);
    }

}
