package com.getcode.dto.study.util;

import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyField;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class StudySpecification {
    public static Specification<Study> equalsRecruitment(Boolean recruitment) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("recruitment"), recruitment));
    }

    public static Specification<Study> equalsSiDo(String siDo) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("siDo"), siDo));
    }
    public static Specification<Study> equalsGuGun(String guGun) {
        return ((root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("guGun"), guGun));
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

    public static Specification<Study> containsFields(List<String> fields) {
        return (root, query, builder) -> {

            if (fields == null || fields.isEmpty()) {
                return null;
            }

            Join<Study, StudyField> subjectsJoin = root.join("study_field", JoinType.LEFT);

            Predicate[] predicates = new Predicate[fields.size()];

            for (int i = 0; i < fields.size(); i++) {
                predicates[i] = builder.equal(subjectsJoin.get("study_field"), fields.get(i));
            }

            return builder.or(predicates);
        };
    }


}
