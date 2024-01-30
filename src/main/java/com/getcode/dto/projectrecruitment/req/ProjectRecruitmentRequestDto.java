package com.getcode.dto.projectrecruitment.req;

import com.getcode.domain.member.Member;
import com.getcode.domain.projectrecruitment.ProjectRecruitment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRecruitmentRequestDto {


        @Length(min = 2, max = 15)
        private String title;
        @Length(min = 2, max = 1000)
        private String content;
        private String siDo;
        private String guGun;
        private boolean online;
        private boolean recruitment;
        private int views;
        private int likeCnt;
        private List<String> subjects;
        private List<String> techStack;

        public ProjectRecruitment toEntity(Member member) {
            return ProjectRecruitment.builder()
                    .title(title)
                    .content(content)
                    .siDo(siDo)
                    .guGun(guGun)
                    .online(online)
                    .views(views)
                    .likeCnt(likeCnt)
                    .recruitment(recruitment)
                    .member(member)
                    .build();
        }


}
