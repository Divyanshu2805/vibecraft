package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.member.InviteMemberRequest;
import com.java.vibecraft.dto.member.MemberResponse;
import com.java.vibecraft.dto.member.UpdateMemberRoleRequest;
import com.java.vibecraft.entity.Project;
import com.java.vibecraft.entity.ProjectMember;
import com.java.vibecraft.entity.ProjectMemberId;
import com.java.vibecraft.entity.User;
import com.java.vibecraft.error.ForbiddenException;
import com.java.vibecraft.error.ResourceNotFoundException;
import com.java.vibecraft.mapper.ProjectMemberMapper;
import com.java.vibecraft.repository.ProjectMemberRepository;
import com.java.vibecraft.repository.ProjectRepository;
import com.java.vibecraft.repository.UserRepository;
import com.java.vibecraft.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    UserRepository userRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {

        Project project = getAccessibleProjectById(projectId, userId);
        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberMapper.toProjectMemberResponseFromOwner(project.getOwner()));
        memberResponseList.addAll(projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList());

        return memberResponseList;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {

        Project project = getAccessibleProjectById(projectId, userId);

        if(!project.getOwner().getId().equals(userId)){
            throw new ForbiddenException("You are not allowed to invite");
        }

        User invitee = userRepository.findByEmail(request.email()).orElseThrow();

        if(invitee.getId().equals(userId)) {
            throw new ForbiddenException("Cannot invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());

        if(projectMemberRepository.existsById(projectMemberId)) {
            throw new ForbiddenException("Cannot invite once again");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(member);

        return projectMemberMapper.toProjectMemberResponseFromMember(member);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {

    }

    public Project getAccessibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
    }
}
