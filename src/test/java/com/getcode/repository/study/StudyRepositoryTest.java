package com.getcode.repository.study;

import static org.assertj.core.api.Assertions.*;

import com.getcode.domain.study.Study;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudyRepositoryTest {
    @Autowired
    private StudyRepository studyRepository;

    @Test
    @DisplayName("StudyRepository 연결 여부 확인")
    public void StudyRepositoryTest() {
        assertThat(studyRepository).isNotNull();
    }

    @Test
    public void findByTitleContainingOrContentContainingTest() {
        //given
        Study study1 = createStudy("Java", "aaaaa");
        Study study2 = createStudy("Kotlin", "Java");
        Study study3 = createStudy("ASCII", "Java is...");
        Study study4 = createStudy("안녕", "this is Java");
        Study study5 = createStudy("Study", "this is 안녕");
        Study study6 = createStudy("Study", "this 안녕 Java");

        studyRepository.saveAll(List.of(study1, study2, study3, study4, study5, study6));

        //when
        List<Study> result = studyRepository.findByTitleOrContentContaining("안녕");

        //then
        assertThat(result).hasSize(3);
    }

    private Study createStudy(String title, String content) {
        return Study.builder()
                .title(title)
                .content(content)
                .region("New York")
                .recruitment(true)
                .online(true)
                .views(0)
                .count(0)
                .build();
    }
}