package com.getcode.service.mypage;

import com.getcode.domain.member.Member;
import com.getcode.domain.project.Project;
import com.getcode.dto.project.res.ProjectInfoResponseDto;
import com.getcode.exception.member.NotFoundMemberException;
import com.getcode.repository.member.MemberRepository;
import com.getcode.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectInfoResponseDto> getMyProject(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        List<Project> projects = member.getProjects();

        List<ProjectInfoResponseDto> myProjectRes = new ArrayList<>();

        projects.forEach(project -> myProjectRes.add(new ProjectInfoResponseDto(project)));

        return myProjectRes;

    }


    @Transactional
    public List<ProjectInfoResponseDto> getMyWishProject(String memberEmail, int size, int page) {

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(NotFoundMemberException::new);

        Pageable pageable = PageRequest.of(page-1, size);

        List<Project> projects = projectRepository.findAllWishProjectByMemberId(member.getId(),pageable);

        List<ProjectInfoResponseDto> myWishProjectRes = new ArrayList<>();

        projects.forEach(project -> myWishProjectRes.add(new ProjectInfoResponseDto(project)));

        return myWishProjectRes;
    }

}
