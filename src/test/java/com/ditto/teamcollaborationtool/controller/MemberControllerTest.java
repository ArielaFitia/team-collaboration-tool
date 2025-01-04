package com.ditto.teamcollaborationtool.controller;

import com.ditto.teamcollaborationtool.dto.MemberDTO;
import com.ditto.teamcollaborationtool.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MemberDTO testMemberDTO;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        testMemberDTO = new MemberDTO();
        testMemberDTO.setId(1L);
        testMemberDTO.setName("test");
        testMemberDTO.setEmail("test@test.com");
        testMemberDTO.setRole("ROLE_USER");
    }

    @Test
    void whenCreateMember_thenReturnCreatedMember() throws Exception {
        when(memberService.createMember(any(MemberDTO.class))).thenReturn(testMemberDTO);

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMemberDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testMemberDTO.getId()))
                .andExpect(jsonPath("$.name").value(testMemberDTO.getName()))
                .andExpect(jsonPath("$.email").value(testMemberDTO.getEmail()))
                .andExpect(jsonPath("$.role").value(testMemberDTO.getRole()));
    }

    @Test
    void whenGetAllMembers_thenReturnAllMembers() throws Exception {
        List<MemberDTO> members = Arrays.asList(testMemberDTO);
        when(memberService.getAllMembers()).thenReturn(members);

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testMemberDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(testMemberDTO.getName()))
                .andExpect(jsonPath("$[0].email").value(testMemberDTO.getEmail()))
                .andExpect(jsonPath("$[0].role").value(testMemberDTO.getRole()));
    }

    @Test
    void whenGetMemberById_thenReturnMember() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(testMemberDTO);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMemberDTO.getId()))
                .andExpect(jsonPath("$.name").value(testMemberDTO.getName()))
                .andExpect(jsonPath("$.email").value(testMemberDTO.getEmail()))
                .andExpect(jsonPath("$.role").value(testMemberDTO.getRole()));
    }

    @Test
    void whenUpdateMember_thenReturnUpdatedMember() throws Exception {
        when(memberService.updateMember(eq(1L), any(MemberDTO.class))).thenReturn(testMemberDTO);

        mockMvc.perform(put("/api/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMemberDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testMemberDTO.getId()))
                .andExpect(jsonPath("$.name").value(testMemberDTO.getName()))
                .andExpect(jsonPath("$.email").value(testMemberDTO.getEmail()))
                .andExpect(jsonPath("$.role").value(testMemberDTO.getRole()));
    }

    @Test
    void whenDeleteMember_thenReturnDeletedMember() throws Exception {
        doNothing().when(memberService).deleteMember(eq(1L));

        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent());
    }
}
