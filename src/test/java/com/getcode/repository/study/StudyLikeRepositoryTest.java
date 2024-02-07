package com.getcode.repository.study;

import com.getcode.domain.member.Member;
import com.getcode.domain.study.Study;
import com.getcode.domain.study.StudyLike;
import com.getcode.repository.member.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StudyLikeRepositoryTest {
    @Autowired
    private StudyLikeRepository studyLikeRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("StudyLikeRepository 연결 여부 확인")
    public void StudyLikeRepositoryTest() {
        Assertions.assertThat(studyLikeRepository).isNotNull();
    }

    @Test
    @DisplayName("사용자 Id, 스터디 Id로 좋아요 여부 확인")
    public void findByMemberIdAndStudyId() {
        //given
        Member member = Member.builder()
                .email("kyun11@naver.com")
                .nickname("김명균")
                .emailVerified(true)
                .build();

        Study study = Study.builder()
                .title("Java")
                .content("Spring")
                .siDo("New York")
                .recruitment(true)
                .online(true)
                .views(0)
                .likeCnt(0)
                .build();

        StudyLike studyLike = StudyLike.builder()
                .member(member)
                .study(study)
                .build();

        memberRepository.save(member);
        studyRepository.save(study);
        StudyLike savedStudyLike = studyLikeRepository.save(studyLike);

        //when
        Optional<StudyLike> like = studyLikeRepository.findByMemberIdAndStudyId(member.getId(), study.getId());

        //then
        Assertions.assertThat(like.get()).isEqualTo(savedStudyLike);
    }
}