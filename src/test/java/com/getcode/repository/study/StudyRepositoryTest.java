package com.getcode.repository.study;

import com.getcode.domain.common.Field;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyField;
import com.getcode.dto.study.util.StudySpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudyRepositoryTest {
    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyFieldRepository studyFieldRepository;

    @Test
    @DisplayName("StudyRepository 연결 여부 확인")
    public void StudyRepositoryTest() {
        assertThat(studyRepository).isNotNull();
    }

    @Test
    public void findByTitleContainingOrContentContainingTest() {
        //given
        Study study1 = createStudy("Java", "aaaaa", 500);
        Study study2 = createStudy("Kotlin", "Java", 400);
        Study study3 = createStudy("ASCII", "Java is...", 300);
        Study study4 = createStudy("안녕", "this is Java", 101);
        Study study5 = createStudy("Study1", "this is 안녕", 200);
        Study study6 = createStudy("Study2", "this is 안녕", 50);
        Study study7 = createStudy("Study3", "this 안녕 Java", 60);
        Study study8 = createStudy("Study4", "this 안녕 Java", 71);
        Study study9 = createStudy("Study5", "this 안녕 Java", 82);
        Study study10 = createStudy("Study6", "this 안녕 Java", 222);
        Study study11 = createStudy("Study7", "this 안녕 Java", 1);
        Study study12 = createStudy("Study8", "this 안녕 Java", 1);
        Study study13 = createStudy("Study9", "this 안녕 Java", 1);
        Study study14 = createStudy("Study10", "this 안녕 Java", 1);
        Study study15 = createStudy("Study11", "this 안녕 Java", 1);

        studyRepository.saveAll(List.of(study1, study2, study3, study4, study5, study6,
                study7, study8, study9, study10, study11, study12, study13, study14, study15));

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "count"));
        List<Study> result = studyRepository.findByTitleOrContentContaining("", pageable);

        //then
        assertThat(result).hasSize(10);
        assertThat(result).extracting("title")
                .containsExactly(
                        "Java", "Kotlin", "ASCII", "Study6",
                        "Study1", "안녕", "Study5", "Study4", "Study3", "Study2"
                );
    }

    @Test
    public void specificationTest() {
        //given
        Study study1 = createStudy("title1", "content1", 0);
        Study study2 = createStudy("title2", "content2", 0);
        Study study3 = createStudy("title3", "title1", 0);
        Study study4 = createStudy("title4", "content4", 0);
        Study study5 = createStudy("title5", "content5", 0);
        studyRepository.saveAll(List.of(study1, study2, study3, study4, study5));
        StudyField ss1 = StudyField.builder()
                .study(study1)
                .field(Field.CS)
                .build();
        StudyField ss2 = StudyField.builder()
                .study(study1)
                .field(Field.CI_CD)
                .build();

        StudyField ss3 = StudyField.builder()
                .study(study2)
                .field(Field.ALGORITHM)
                .build();
        StudyField s4 = StudyField.builder()
                .study(study4)
                .field(Field.INTERVIEW)
                .build();

        studyFieldRepository.saveAll(List.of(ss1, ss2, ss3, s4));

        //when
        Specification<Study> spec = (root, query, criteriaBuilder) -> null;
        spec = spec.and(StudySpecification.equalsSiDo("LA"));
        spec = spec.and(StudySpecification.equalsGuGun("LA"));
        spec = spec.and(StudySpecification.equalsRecruitment(true));
        spec = spec.and(StudySpecification.equalsOnline(true));
        spec = spec.and(StudySpecification.equalsYear(2024));
        spec = spec.and(StudySpecification.containsFields(List.of("CS")));
        spec = spec.and(StudySpecification.equalsKeyword("title1"));
        List<Study> studies = studyRepository.findAll(spec);

        //then
        assertThat(studies).hasSize(1);

    }

    private Study createStudy(String title, String content, int count) {
        return Study.builder()
                .title(title)
                .content(content)
                .siDo("LA")
                .recruitment(true)
                .online(true)
                .views(0)
                .likeCnt(count)
                .build();
    }

}