package com.ditto.teamcollaborationtool.service;

import com.ditto.teamcollaborationtool.dto.MemberDTO;
import com.ditto.teamcollaborationtool.model.Member;
import com.ditto.teamcollaborationtool.model.Task;
import com.ditto.teamcollaborationtool.repository.MemberRepository;
import com.ditto.teamcollaborationtool.repository.TaskRepository;
import com.ditto.teamcollaborationtool.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;
    private MemberDTO memberDTO;
    private Task task;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "Member A", "Member A email", "software engineer");
        memberDTO = new MemberDTO(1L, "Member A", "Member A email", "software engineer");

        task = new Task();
        task.setId(1L);
        task.setName("Task A");
    }

    @Test
    void createMember_ShouldReturnSavedMember() {
        when(modelMapper.map(memberDTO, Member.class)).thenReturn(member);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDTO);

        MemberDTO result = memberService.createMember(memberDTO);

        assertNotNull(result);
        assertEquals(member.getName(), result.getName());
    }

    @Test
    void getMemberById_ShouldReturnMember_WhenMemberIsFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDTO);

        MemberDTO result = memberService.getMemberById(1L);

        assertNotNull(result);
        assertEquals(member.getName(), result.getName());
        assertEquals(member.getEmail(), result.getEmail());
    }

    @Test
    void getAllMembers_ShouldReturnAllMembers() {
        Member memberB = new Member(2L, "Member B", "Member B email", "software engineer");
        MemberDTO memberDTOB = new MemberDTO(2L, "Member B", "Member B email", "software engineer");
        List<Member> members = Arrays.asList(member, memberB);

        when(memberRepository.findAll()).thenReturn(members);
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDTO);
        when(modelMapper.map(memberB, MemberDTO.class)).thenReturn(memberDTOB);

        List<MemberDTO> result = memberService.getAllMembers();

        assertNotNull(result);
        assertEquals(members.size(), result.size());
        assertEquals(member.getName(), result.get(0).getName());
        assertEquals(memberB.getName(), result.get(1).getName());
    }

    @Test
    void UpdateMember_ShouldReturnUpdatedMember_WhenMemberIsFound() {
        Long memberId = 1L;
        MemberDTO updateRequest = new MemberDTO(memberId, "Updated member", "updated email", "updated role");
        Member existingMember = new Member(memberId, "Old member", "Old email", "old role");
        Member updatedMember = new Member(memberId, "Updated member", "Updated email", "Updated role");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);
        when(modelMapper.map(updatedMember, MemberDTO.class)).thenReturn(updateRequest);

        MemberDTO result = memberService.updateMember(memberId, updateRequest);

        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getEmail(), result.getEmail());
        assertEquals(updateRequest.getRole(), result.getRole());
    }

    @Test
    void deleteMember_ShouldCallRepositoryDelete() {
        memberService.deleteMember(1L);

        verify(memberRepository).deleteById(1L);
    }

    @Test
    void addTaskToMember_ShouldReturnUpdatedMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDTO);

        MemberDTO result = memberService.addTaskToMember(1L, 1L);

        assertNotNull(result);
        verify(memberRepository).save(member);
        verify(taskRepository).save(task);
    }

    @Test
    void removeTaskFromMember_ShouldReturnUpdatedMember() {
        member.getTasks().add(task);
        task.setMember(member);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(modelMapper.map(member, MemberDTO.class)).thenReturn(memberDTO);

        MemberDTO result = memberService.removeTaskFromMember(1L, 1L);

        assertNotNull(result);
        verify(memberRepository).save(member);
        verify(taskRepository).save(task);
    }
}
