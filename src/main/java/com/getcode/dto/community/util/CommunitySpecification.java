package com.getcode.dto.community.util;

import com.getcode.domain.community.Community;
import com.getcode.domain.common.CommunityCategory;
import com.getcode.domain.study.Study;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CommunitySpecification {

    public static Specification<Community> equalsCategory(String category) {
        return (root, query, builder) -> {

            CommunityCategory category1 = CommunityCategory.fromString(category);

            return builder.equal(root.get("category"), category1);
        };
    }

    public static Specification<Community> equalsKeyword(String keyword) {
        return (root, query, builder) -> {

            if (keyword == null || keyword.isEmpty()) {
                return null;
            }

            Predicate titleCondition = builder.like(root.get("title"), "%" + keyword + "%");
            Predicate contentCondition = builder.like(root.get("content"), "%" + keyword + "%");

            return builder.or(titleCondition, contentCondition);
        };
    }

}
