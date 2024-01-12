package com.getcode.repository.study;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudyCommentRepositoryTest {

    @Autowired
    private StudyCommentRepository studyCommentRepository;
    @Test
    @DisplayName("StudyCommentRepository 연결 여부 확인")
    public void StudyCommentRepositoryTest() {
        assertThat(studyCommentRepository).isNotNull();
    }

}