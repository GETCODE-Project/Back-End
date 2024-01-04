package com.getcode.controller.project;

import com.getcode.config.s3.S3Service;
import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.common.Subject;
import com.getcode.domain.project.ProjectImage;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.s3.S3FileDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> addProject(@Parameter(description = "프로젝트 등록 값", required = true)
                                            @Valid @RequestPart ProjectRequestDto projectRequestDto,
                                        @Parameter(description = "프로젝트 이미지")
                                        @RequestPart(name = "fileType") String fileType,
                                        @RequestPart(name = "files") List<MultipartFile> multipartFiles
    ){


        List<S3FileDto> files = s3Service.uploadFiles(fileType, multipartFiles);
        //파일 url리스트로 변환
        List<String> fileUrls = files.stream()
                                        .map(S3FileDto::getUploadFileUrl)
                                        .collect(Collectors.toList());

        //ProjectImage 타입의 url로 변환
        List<ProjectImage> imageUrls = fileUrls.stream()
                        .map(url -> ProjectImage.builder().imageUrl(url).build())
                                .collect(Collectors.toList());

        projectRequestDto.setImageUrls(imageUrls);

        projectService.insertProject(projectRequestDto);


        return ResponseEntity.ok().body(HttpStatus.OK);

    }




    @Operation(summary = "프로젝트 삭제 api")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@Parameter(description = "프로젝트 아이디") @PathVariable Long id){

        String memberId = SecurityUtil.getCurrentMemberId();

        int res = 0;

        res = projectService.deleteProject(id, Long.parseLong(memberId));

        if(res <= 0 ){
            throw new RuntimeException();
        }

            return ResponseEntity.ok().body("삭제가 완료되었습니다.");
    }









}
