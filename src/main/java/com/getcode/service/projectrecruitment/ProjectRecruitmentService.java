package com.getcode.service.projectrecruitment;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentComment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentLike;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;
import com.getcode.dto.projectrecruitment.req.*;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.NotFoundCommentException;
import com.getcode.exception.project.NotMatchMemberException;
import com.getcode.exception.project.NotOwnLikeException;
import com.getcode.exception.projectrecruitment.NotFoundProjectRecruitmentException;
import com.getcode.repository.MemberRepository;
import com.getcode.repository.projectrecruitment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectRecruitmentService {


    private final MemberRepository memberRepository;
    private final ProjectRecruitmentRepository projectRecruitmentRepository;
    private final ProjectRecruitmentStackRepository projectRecruitmentStackRepository;
    private final ProjectRecruitmentSubjectRepository projectRecruitmentSubjectRepository;
    private final ProjectRecruitmentCommentRepository projectRecruitmentCommentRepository;
    private final ProjectRecruitmentLikeRepository projectRecruitmentLikeRepository;


    @Transactional
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

    @Transactional
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

    @Transactional
    public void addComment(Long id, RecruitmentCommentRequestDto requestDto) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(id).orElseThrow(NotFoundProjectRecruitmentException::new);

        projectRecruitmentCommentRepository.save(requestDto.toEntity(projectRecruitment, member));

    }

    @Transactional
    public void updateComment(Long projectId, Long commentId, RecruitmentCommentUpdateDto requestDto) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitmentComment projectRecruitmentComment = projectRecruitmentCommentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        if(!member.getEmail().equals(projectRecruitmentComment.getMember().getEmail())){
            throw new NotMatchMemberException();
        }

        if(!projectRecruitmentComment.getProjectRecruitment().getId().equals(projectId)){
            throw new NotFoundCommentException();
        }

        projectRecruitmentComment.update(requestDto);



    }

    public void deleteComment(Long projectId, Long commentId) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitmentComment projectRecruitmentComment = projectRecruitmentCommentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);

        if(!member.getEmail().equals(projectRecruitmentComment.getMember().getEmail())){
            throw new NotMatchMemberException();
        }

        if(!projectRecruitmentComment.getProjectRecruitment().getId().equals(projectId)){
            throw new NotFoundCommentException();
        }

        projectRecruitmentCommentRepository.deleteById(commentId);


    }

    @Transactional
    public int likeProjectRecruitment(Long projectId) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(projectId).orElseThrow(NotFoundProjectRecruitmentException::new);

        ProjectRecruitmentLike projectRecruitmentLike = projectRecruitmentLikeRepository.findByProjectRecruitmentAndMember(projectRecruitment, member);

        String owner = member.getEmail();

        if(owner == projectRecruitment.getMember().getEmail()){
            throw new NotOwnLikeException();
        }

        if(projectRecruitmentLike != null){
            projectRecruitmentLikeRepository.delete(projectRecruitmentLike);
            projectRecruitment.likeCntDown();

            projectRecruitmentRepository.save(projectRecruitment);
            return -1;

        } else {
            projectRecruitmentLikeRepository.save(
                    ProjectRecruitmentLike.builder()
                            .projectRecruitment(projectRecruitment)
                            .member(member)
                            .build());

            projectRecruitment.likeCntUp();
            projectRecruitmentRepository.save(projectRecruitment);
            return 1;
        }










    }
}
