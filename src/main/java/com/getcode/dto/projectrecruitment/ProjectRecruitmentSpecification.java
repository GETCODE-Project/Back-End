package com.getcode.dto.projectrecruitment;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.project.Project;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentSubject;
import com.getcode.domain.projectrecruitment.ProjectRecruitmentTech;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectRecruitmentSpecification {

    //기술스택 조건 검사
    public static Specification<ProjectRecruitment> techStackLike(List<String> techStacks){
        return (root, query, criteriaBuilder) -> {

            Join<ProjectRecruitment, ProjectRecruitmentTech> projectRecruitmentTechJoin = root.join("techStacks", JoinType.LEFT);
            Predicate[] predicates = new Predicate[techStacks.size()];
            //여러개의 기술을 검색할 경우 배열에 저장하는 for문
            for(int i=0; i<techStacks.size(); i++){
                TechStack stack = TechStack.fromString(techStacks.get(i));
                predicates[i] = criteriaBuilder.equal(projectRecruitmentTechJoin.get("techStack"), stack);
            }
            //저장한 배열을 리턴
            return criteriaBuilder.or(predicates);
        };

    }


    //주제 조건 검사
    public static Specification<ProjectRecruitment> subjectLike(String subjects){
        return (root, query, criteriaBuilder) -> {

            Join<ProjectRecruitment, ProjectRecruitmentSubject> projectRecruitmentSubjectJoin = root.join("subjects", JoinType.LEFT);
            Predicate predicate;

                Subject subject = Subject.fromString(subjects);
                predicate = criteriaBuilder.equal(projectRecruitmentSubjectJoin.get("subject"), subject);

            return criteriaBuilder.or(predicate);
        };
    }


    public static Specification<ProjectRecruitment> yearBetween(Integer year){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.function("date_format", Integer.class, root.get("modifiedDate"), criteriaBuilder.literal("%Y")), year);
    }


    public static Specification<ProjectRecruitment> keywordLikeTitleOrContent(String keyword){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("content"), "%" + keyword + "%")
                )));

    }

    public static Specification<ProjectRecruitment> combineSpecifications(List<Specification<ProjectRecruitment>> specifications) {
        Specification<ProjectRecruitment> combinedSpec = Specification.where(null);

        for (Specification<ProjectRecruitment> spec : specifications) {
            combinedSpec = combinedSpec.and(spec);
        }

        return combinedSpec;
    }



}
