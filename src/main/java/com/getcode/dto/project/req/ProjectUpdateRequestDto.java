package com.getcode.dto.project.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequestDto {

    private String title;

    private String content;

    private String introduction;

    private String githubUrl;

    //private List<String> imageUrls;

    private List<String> techStackList;

    private String subject;


}
