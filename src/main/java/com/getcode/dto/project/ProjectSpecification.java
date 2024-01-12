package com.getcode.dto.project;

import com.getcode.domain.project.Project;
import com.getcode.domain.project.ProjectSubject;
import com.getcode.domain.project.ProjectTech;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpecification  {

    public static Specification<Project> techStackLike(List<ProjectTech> techStack){
        return ((root, query, criteriaBuilder) -> root.get("techStacks").in(techStack.stream().map(ProjectTech::getTechStack).toArray()));
    }

    public static Specification<Project> subjectLike(List<ProjectSubject> subjects){
        return ((root, query, criteriaBuilder) -> root.get("projectSubjects").in(subjects.stream().map(ProjectSubject::getSubject).toArray()));
    }

    public static Specification<Project> yearBetween(String year){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.function("date_format", String.class, root.get("createDate"), criteriaBuilder.literal("%Y")), year)));
    }

    public static Specification<Project> keywordLikeTitle(String keyword){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%")));
    }

    public static Specification<Project> keywordLikeContent(String keyword){
        return (((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + keyword + "%")));
    }

    public static Specification<Project> combineSpecifications(List<Specification<Project>> specifications) {
        Specification<Project> combinedSpec = Specification.where(null);

        for (Specification<Project> spec : specifications) {
            combinedSpec = combinedSpec.and(spec);
        }

        return combinedSpec;
    }

}


