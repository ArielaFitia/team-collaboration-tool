package com.ditto.teamcollaborationtool.service.impl;

import com.ditto.teamcollaborationtool.dto.MemberDTO;
import com.ditto.teamcollaborationtool.model.Member;
import com.ditto.teamcollaborationtool.model.Task;
import com.ditto.teamcollaborationtool.repository.MemberRepository;
import com.ditto.teamcollaborationtool.repository.TaskRepository;
import com.ditto.teamcollaborationtool.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, ModelMapper modelMapper, TaskRepository taskRepository) {
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = modelMapper.map(memberDTO, Member.class);
        member = memberRepository.save(member);
        return modelMapper.map(member, MemberDTO.class);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        return modelMapper.map(member, MemberDTO.class);
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> modelMapper.map(member, MemberDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setRole(memberDTO.getRole());
        member = memberRepository.save(member);
        return modelMapper.map(member, MemberDTO.class);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public MemberDTO addTaskToMember(Long memberId, Long taskId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setMember(member);
        if(member.getTasks() == null) {
            member.setTasks(new ArrayList<>());
        }
        member.getTasks().add(task);

        taskRepository.save(task);
        Member updatedMember = memberRepository.save(member);

        return modelMapper.map(updatedMember, MemberDTO.class);
    }

    @Override
    public MemberDTO removeTaskFromMember(Long memberId, Long taskId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setMember(null);
        if(member.getTasks() != null) {
            member.getTasks().remove(task);
        }

        taskRepository.save(task);
        Member updatedMember = memberRepository.save(member);

        return modelMapper.map(updatedMember, MemberDTO.class);
    }
}
