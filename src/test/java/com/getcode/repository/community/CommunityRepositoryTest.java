package com.getcode.repository.community;

import static org.assertj.core.api.Assertions.*;

import com.getcode.domain.community.Community;
import com.getcode.domain.common.CommunityCategory;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommunityRepositoryTest {
    @Autowired
    private CommunityRepository communityRepository;
    @Test
    public void CommunityFindTest() {
        //given
        Community community1 = createCommunity("제목1", "본문1");
        Community community2 = createCommunity("제목2", "본문1");
        Community community3 = createCommunity("제목3", "본문1");
        communityRepository.saveAll(List.of(community1, community2, community3));
        System.out.println(CommunityCategory.QNA.equals(CommunityCategory.valueOf("qna".toUpperCase())));
        //when
        List<Community> community = communityRepository.findAll();

        //then
        assertThat(community).hasSize(3);
//        assertThat(community).extracting("category")
//                .containsExactlyInAnyOrder(CommunityCategory.QNA, CommunityCategory.COUNSEL, CommunityCategory.COUNSEL);
    }

    private Community createCommunity(String title, String content) {
        return Community.builder()
                .title(title)
                .content(content)
                .views(0)
                .likeCnt(0)
//                .category(CommunityCategory.valueOf(type))
                .build();
    }
}