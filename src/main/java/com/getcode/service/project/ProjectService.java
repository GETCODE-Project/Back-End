package com.getcode.service.project;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.dto.project.req.ProjectRequestDto;
import com.getcode.dto.s3.S3FileUpdateDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.project.ProjectRepository;
import com.getcode.repository.project.ProjectStackRepository;
import com.getcode.repository.project.ProjectSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.getcode.config.security.SecurityUtil.getCurrentMemberId;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectSubjectRepository projectSubjectRepository;
    private final MemberRepository memberRepository;

    /*
    public int deleteProject(Long id, long memberId) {

        projectRepository.deleteById(id);

        return 1;
    }

     */

    @Transactional
    public void insertProject(ProjectRequestDto projectRequestDto, S3FileUpdateDto fileUrl) {

        Member member = memberRepository.findById(Long.parseLong(getCurrentMemberId())).orElseThrow(NotFoundMemberException::new);



        projectRequestDto.setImageUrl(fileUrl.getImageUrl());



        Project saveProject = projectRepository.save(projectRequestDto.toEntity(member));

        projectStackRepository.save(projectRequestDto.toEntity(saveProject));
        projectSubjectRepository.save(projectRequestDto.Entity(saveProject));

    }

}
