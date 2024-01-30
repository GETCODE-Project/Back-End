package com.getcode.repository.study;

import com.getcode.domain.study.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {
}
