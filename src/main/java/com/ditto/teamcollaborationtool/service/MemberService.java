package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    MemberDTO createMember(MemberDTO memberDTO);
    MemberDTO getMemberById(Long id);
    List<MemberDTO> getAllMembers();
    MemberDTO updateMember(Long id, MemberDTO memberDTO);
    void deleteMember(Long id);
    MemberDTO addTaskToMember(Long memberId, Long taskId);
    MemberDTO removeTaskFromMember(Long memberId, Long taskId);
}
