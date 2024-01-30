package com.getcode.domain.study;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class StudySpecification {
    public static Specification<Study> equalsRecruitment(Boolean recruitment) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("recruitment"), recruitment));
    }

    public static Specification<Study> equalsRegion(String region) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("region"), region));
    }

    public static Specification<Study> equalsOnline(Boolean online) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("online"), online));
    }

    public static Specification<Study> equalsYear(Integer year) {
        return (root, query, builder) -> builder.equal(builder.function("year", Integer.class, root.get("modifiedDate")), year);
    }

    public static Specification<Study> equalsKeyword(String keyword) {
        return (root, query, builder) -> {

            if (keyword == null || keyword.isEmpty()) {
                return null;
            }

            Predicate titleCondition = builder.like(root.get("title"), "%" + keyword + "%");
            Predicate contentCondition = builder.like(root.get("content"), "%" + keyword + "%");

            return builder.or(titleCondition, contentCondition);
        };
    }

    public static Specification<Study> containsSubjects(List<String> subjectList) {
        return (root, query, builder) -> {

            if (subjectList == null || subjectList.isEmpty()) {
                return null;
            }

            Join<Study, StudySubject> subjectsJoin = root.join("subjects", JoinType.LEFT);

            Predicate[] predicates = new Predicate[subjectList.size()];

            for (int i = 0; i < subjectList.size(); i++) {
                predicates[i] = builder.equal(subjectsJoin.get("subject"), subjectList.get(i));
            }

            return builder.or(predicates);
        };
    }


}
