package com.getcode.dto.project;

import com.getcode.domain.common.Subject;
import com.getcode.domain.common.TechStack;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectTech;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpecification  {

    public static Specification<Project> techStackLike(List<String> techStacks){
        return (root, query, criteriaBuilder) -> {
            Join<Project, ProjectTech> projectTechJoin = root.join("techStacks", JoinType.LEFT);
            Predicate[] predicates = new Predicate[techStacks.size()];

            for (int i = 0; i < techStacks.size(); i++) {

                TechStack stack = TechStack.fromString(techStacks.get(i));

                predicates[i] = criteriaBuilder.equal(projectTechJoin.get("techStack"), stack);
            }

            return criteriaBuilder.or(predicates);
        };
    }


    public static Specification<Project> subjectLike(String subject) {
        return (root, query, criteriaBuilder) ->
        {
            Subject subject1 = Subject.fromString(subject);
            return criteriaBuilder.equal(root.get("subject"), subject1);
        };
    }

    public static Specification<Project> yearBetween(Integer year){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.function("date_format", Integer.class, root.get("modifiedDate"), criteriaBuilder.literal("%Y")), year)));
    }

    public static Specification<Project> keywordLikeTitleOrContentOrIntroduction(String keyword){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("content"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("introduction"), "%" + keyword + "%")
                )));

    }

    public static Specification<Project> combineSpecifications(List<Specification<Project>> specifications) {
        Specification<Project> combinedSpec = Specification.where(null);

        for (Specification<Project> spec : specifications) {
            combinedSpec = combinedSpec.and(spec);
        }

        return combinedSpec;
    }

}


