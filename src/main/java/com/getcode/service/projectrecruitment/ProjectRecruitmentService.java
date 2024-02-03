package com.getcode.service.projectrecruitment;

import com.getcode.config.security.SecurityUtil;
import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectLike;
import com.getcode.domain.project.WishProject;
import com.getcode.domain.projectrecruitment.*;
import com.getcode.dto.project.ProjectSpecification;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.dto.projectrecruitment.ProjectRecruitmentSpecification;
import com.getcode.dto.projectrecruitment.req.*;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentDetailResDto;
import com.getcode.dto.projectrecruitment.res.ProjectRecruitmentInfoResDto;
import com.getcode.dto.projectrecruitment.res.RecruitmentCommentResDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.exception.project.NotFoundCommentException;
import com.getcode.exception.project.NotMatchMemberException;
import com.getcode.exception.project.NotOwnLikeException;
import com.getcode.exception.project.NotOwnWishException;
import com.getcode.exception.projectrecruitment.NotFoundProjectRecruitmentException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.projectrecruitment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ProjectRecruitmentWishRepository projectRecruitmentWishRepository;


    //프로젝트 모집글 등록
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
    //프로젝트 모집글 삭제
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
    //프로젝트 모집글 댓글 등록
    @Transactional
    public void addComment(Long id, RecruitmentCommentRequestDto requestDto) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(id).orElseThrow(NotFoundProjectRecruitmentException::new);

        projectRecruitmentCommentRepository.save(requestDto.toEntity(projectRecruitment, member));

    }
    //프로젝트 모집글 댓글 수정
    @Transactional
    public void updateComment(Long recruitmentId, Long id, RecruitmentCommentUpdateDto requestDto) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitmentComment projectRecruitmentComment = projectRecruitmentCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);

        if(!member.getEmail().equals(projectRecruitmentComment.getMember().getEmail())){
            throw new NotMatchMemberException();
        }

        if(!projectRecruitmentComment.getProjectRecruitment().getId().equals(recruitmentId)){
            throw new NotFoundCommentException();
        }

        projectRecruitmentComment.update(requestDto);



    }
    //프로젝트 모집글 댓글 삭제
    @Transactional
    public void deleteComment(Long recruitmentId, Long id) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitmentComment projectRecruitmentComment = projectRecruitmentCommentRepository.findById(id).orElseThrow(NotFoundCommentException::new);

        if(!member.getEmail().equals(projectRecruitmentComment.getMember().getEmail())){
            throw new NotMatchMemberException();
        }

        if(!projectRecruitmentComment.getProjectRecruitment().getId().equals(recruitmentId)){
            throw new NotFoundCommentException();
        }

        projectRecruitmentCommentRepository.deleteById(id);


    }
    //프로젝트 모집글 좋아요
    @Transactional
    public int likeProjectRecruitment(Long recruitmentId) {

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundProjectRecruitmentException::new);

        ProjectRecruitmentLike projectRecruitmentLike = projectRecruitmentLikeRepository.findByProjectRecruitmentAndMember(projectRecruitment, member);



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
    //프로젝트 모집글 찜
    @Transactional
    public int wishProjectRecruitment(Long recruitmentId) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(NotFoundMemberException::new);
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(recruitmentId).orElseThrow(NotFoundProjectRecruitmentException::new);

        WishProjectRecruitment wishProjectRecruitment = projectRecruitmentWishRepository.findByProjectRecruitmentAndMember(projectRecruitment, member);


        if(wishProjectRecruitment != null){
            projectRecruitmentWishRepository.delete(wishProjectRecruitment);

            projectRecruitmentRepository.save(projectRecruitment);
            return -1;

        } else {
            projectRecruitmentWishRepository.save(
                    WishProjectRecruitment.builder()
                            .projectRecruitment(projectRecruitment)
                            .member(member)
                            .build());

            projectRecruitmentRepository.save(projectRecruitment);
            return 1;
        }


    }
    //프로젝트 모집글 상세조회
    @Transactional
    public ProjectRecruitmentDetailResDto getDetailRecruitment(Long id) {
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(id).orElseThrow(NotFoundProjectRecruitmentException::new);
        projectRecruitment.viewCntUp();

        ProjectRecruitmentLike projectRecruitmentLike = projectRecruitmentLikeRepository.findByProjectRecruitment(projectRecruitment);
        WishProjectRecruitment wishProjectRecruitment = projectRecruitmentWishRepository.findByProjectRecruitment(projectRecruitment);

        ProjectRecruitmentDetailResDto resDto =  new ProjectRecruitmentDetailResDto(projectRecruitment, projectRecruitmentLike, wishProjectRecruitment);
        return resDto;
    }

    //프로젝트 모집글 전체 조회
    @Transactional(readOnly = true)
    public List<ProjectRecruitmentInfoResDto> getAllRecuritment(String sort, int page, int size, String keyword,
                                                                List<String> subject, List<String> techStack, Integer year,
                                                                Long memberId) {

        Sort sortCriteria;

        if(sort.equals("pastOrder")){
            sortCriteria = Sort.by(Sort.Direction.ASC, "modifiedDate");
        } else if (sort.equals("likeCnt")) {
            sortCriteria = Sort.by(Sort.Direction.DESC, "likeCnt");
        } else {
            sortCriteria = Sort.by(Sort.Direction.DESC, "modifiedDate");
        }

        Pageable pageable = PageRequest.of(page -1, size, sortCriteria);

        List<Specification<ProjectRecruitment>> specifications = new ArrayList<>();

        if (!techStack.isEmpty() && techStack != null) {
            specifications.add(ProjectRecruitmentSpecification.techStackLike(techStack));
        }

        if (!subject.isEmpty() && subject != null) {
            specifications.add(ProjectRecruitmentSpecification.subjectLike(subject));
        }

        if (year != null) {
            specifications.add(ProjectRecruitmentSpecification.yearBetween(year));
        }

        if (keyword != null && !keyword.isEmpty()) {
            specifications.add(ProjectRecruitmentSpecification.keywordLikeTitleOrContent(keyword));
        }



        Specification<ProjectRecruitment> combinedSpec = ProjectRecruitmentSpecification.combineSpecifications(specifications);

        Page<ProjectRecruitment> recruitmentPage = projectRecruitmentRepository.findAll(combinedSpec, pageable);

        List<ProjectRecruitmentInfoResDto> responseDto = new ArrayList<>();

        recruitmentPage.forEach(recruitment -> {

            ProjectRecruitmentInfoResDto dto = new ProjectRecruitmentInfoResDto(recruitment);
            if(memberId != null) {
                dto.setCheckLike(isRecruitmentLikedByUser(recruitment.getId(), memberId));
                dto.setCheckWish(isRecruitmentWishedByUser(recruitment.getId(), memberId));
            }
            responseDto.add(dto);
        });

        return responseDto;
    }

    //프로젝트 모집글 수정
    @Transactional
    public void updateRecruitment(RecruitmentUpdateRequestDto requestDto, Long id) {

        String memberEmail = SecurityUtil.getCurrentMemberEmail();
        ProjectRecruitment projectRecruitment = projectRecruitmentRepository.findById(id).orElseThrow(NotFoundProjectRecruitmentException::new);

            if (projectRecruitment.getMember().getEmail().equals(memberEmail)) {
                projectRecruitment.update(requestDto);
                projectRecruitmentRepository.save(projectRecruitment);
            } else {
                throw new NotMatchMemberException();
            }


    }

    private Boolean isRecruitmentLikedByUser(Long projectRecruitmentId, Long memberId) {
        return projectRecruitmentLikeRepository.existsByProjectRecruitmentIdAndMemberId(projectRecruitmentId, memberId);
    }

    private Boolean isRecruitmentWishedByUser(Long projectRecruitmentId, Long memberId) {
        return projectRecruitmentWishRepository.existsByProjectRecruitmentIdAndMemberId(projectRecruitmentId, memberId);
    }


    @Transactional(readOnly = true)
    public List<RecruitmentCommentResDto> getRecruitmentComment(Long recruitmentId) {
        List<ProjectRecruitmentComment> projectRecruitmentComments = projectRecruitmentCommentRepository.findByProjectRecruitmentId(recruitmentId);
        List<RecruitmentCommentResDto> dtos = new ArrayList<>();

        for(ProjectRecruitmentComment projectRecruitmentComment : projectRecruitmentComments){
            dtos.add(new RecruitmentCommentResDto(projectRecruitmentComment));
        }
        return dtos;

    }
}
