package com.getcode.controller;

import com.getcode.domain.common.EnumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "주제, 기술스택 관련 API 명세")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ListController {
    private final EnumService enumService;

    @GetMapping("/techStacks")
    public ResponseEntity<List<String>> techStacks() {
        return ResponseEntity.ok(enumService.getTechStackList());
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> subjects(){
        return ResponseEntity.ok(enumService.getSubjectList());
    }

    @GetMapping("/studyFiled")
    public ResponseEntity<List<String>> studyFiled(){
        return ResponseEntity.ok(enumService.getStudyFieldList());
    }
}
