package com.getcode.domain.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnumService {
    public List<String> getSubjectList(){
        return Subject.subjectList();
    }

    public List<String> getTechStackList(){
        return TechStack.techStackList();
    }

    public List<String> getStudyFieldList(){
        return Field.studyFieldList();
    }
}
