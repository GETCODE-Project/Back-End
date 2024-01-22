package com.getcode.service.mypage;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ProjectInfoResponseDto> getMyProject(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        List<Project> projects = member.getProjects();

        List<ProjectInfoResponseDto> myProjectRes = new ArrayList<>();

        projects.forEach(project -> myProjectRes.add(new ProjectInfoResponseDto(project)));

        return myProjectRes;

    }



    public List<ProjectInfoResponseDto> getMyWishProject(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        member.getWishProject();


    }

}
