package com.getcode.repository.project;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.domain.project.WishProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectWishRepository extends JpaRepository<WishProject, Long> {
    WishProject findByProjectAndMember(Project project, Member member);
}
