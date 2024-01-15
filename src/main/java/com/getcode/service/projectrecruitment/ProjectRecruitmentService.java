package com.getcode.service.projectrecruitment;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;
import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentRequestDto;
import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentSubjectDto;
import com.getcode.dto.projectrecruitment.req.ProjectRecruitmentTechDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.NotMatchMemberException;
import com.getcode.exception.projectrecruitment.NotFoundProjectRecruitmentException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.projectrecruitment.ProjectRecruitmentRepository;
import com.getcode.repository.projectrecruitment.ProjectRecruitmentStackRepository;
import com.getcode.repository.projectrecruitment.ProjectRecruitmentSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectRecruitmentService {


    private final MemberRepository memberRepository;
    private final ProjectRecruitmentRepository projectRecruitmentRepository;
    private final ProjectRecruitmentStackRepository projectRecruitmentStackRepository;
    private final ProjectRecruitmentSubjectRepository projectRecruitmentSubjectRepository;



    public void insertProjectRecruitment(ProjectRecruitmentRequestDto requestDto) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);

        ProjectRecruitment projectRecruitment = requestDto.toEntity(member);

        projectRecruitmentRepository.save(projectRecruitment);

        List<String> techStackList = requestDto.getTechStack();
        List<String> subjectList = requestDto.getSubjects();

        for(String techStack : techStackList){
            projectRecruitmentStackRepository.save(ProjectRecruitmentTechDto.toEntity(projectRecruitment, techStack));
        }
        for(String subject : subjectList){
            projectRecruitmentSubjectRepository.save(ProjectRecruitmentSubjectDto.toEntity(projectRecruitment, subject));
        }


    }

    public int deleteProjectRecruitment(Long id) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(id).orElseThrow(NotFoundProjectRecruitmentException::new);

        int result = 0;

        if(member == projectRecruitment.getMember()){
            projectRecruitmentRepository.deleteById(id);
            return  result = 1;
        }else {
            throw new NotMatchMemberException();
        }

    }
}
