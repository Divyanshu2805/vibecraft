package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.member.InviteMemberRequest;
import com.java.vibecraft.dto.member.MemberResponse;
import com.java.vibecraft.dto.member.UpdateMemberRoleRequest;
import com.java.vibecraft.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {

    }
}
