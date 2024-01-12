package com.getcode.repository.study;

import com.getcode.domain.study.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<List<Study>> findAllByOrderByModifiedDateDesc();
    @Query("SELECT s FROM Study s WHERE s.title LIKE %:keyword% OR s.content LIKE %:keyword%")
    List<Study> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
}
