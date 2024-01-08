package com.getcode.repository.study;

import com.getcode.domain.study.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<List<Study>> findAllByOrderByModifiedDateDesc();
}