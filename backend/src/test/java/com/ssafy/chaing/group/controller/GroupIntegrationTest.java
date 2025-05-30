package com.ssafy.chaing.group.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.group.controller.request.JoinGroupRequest;
import com.ssafy.chaing.group.controller.response.GroupResponse;
import com.ssafy.chaing.group.domain.GroupUserEntity;
import com.ssafy.chaing.group.repository.GroupUserRepository;
import com.ssafy.chaing.group.service.GroupService;
import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GroupIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupUserRepository groupUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity user0;
    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUpUsers() {
        user0 = UserEntity.builder()
                .emailAddress("test0@test.com")
                .password(passwordEncoder.encode("password1!"))
                .name("test0")
                .roleType(RoleType.USER)
                .build();

        user1 = UserEntity.builder()
                .emailAddress("test1@test.com")
                .password(passwordEncoder.encode("password1!"))
                .name("test1")
                .roleType(RoleType.USER)
                .build();

        user2 = UserEntity.builder()
                .emailAddress("test2@test.com")
                .password(passwordEncoder.encode("password1!"))
                .name("test2")
                .roleType(RoleType.USER)
                .build();

        userRepository.saveAll(List.of(user0, user1, user2));
    }

    @Test
    void createGroup() throws Exception {
        // Given
        setSecurityContext(String.valueOf(user0.getId()));

        // 요청 body
        String requestBody = String.format(
                """
                        {
                            "groupName": "테스트 그룹",
                            "maxParticipants": 5,
                            "ownerNickname": "%s",
                            "ownerProfileImage": "profile.png"
                        }
                        """,
                user0.getName() + "의 닉네임"
        );

        // When
        MvcResult result = mockMvc.perform(
                        post("/api/v1/groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode dataNode = root.get("data");
        GroupResponse response = objectMapper.treeToValue(dataNode, GroupResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("테스트 그룹");
        assertThat(response.getInviteCode()).isNotNull(); // 초대 코드 응답 포함 확인
        assertThat(response.getMaxParticipants()).isEqualTo(5);

        UserEntity updatedUser = userRepository.findById(user0.getId()).orElseThrow();

        assertThat(updatedUser.getNickname()).isEqualTo(user0.getName() + "의 닉네임");
        assertThat(updatedUser.getProfileImage()).isEqualTo("profile.png");

    }

    @Test
    void getGroupByInviteCode() throws Exception {
        // Given
        setSecurityContext(String.valueOf(user0.getId()));

        // 그룹 생성
        GroupDTO createdGroup = groupService.createGroup(
                new CreateGroupCommand(
                        user0.getId(),
                        user0.getName(),
                        "profile.png",
                        "테스트 그룹",
                        5
                )
        );

        String inviteCode = createdGroup.getInviteCode();

        // When
        MvcResult result = mockMvc.perform(
                        get("/api/v1/groups/search")
                                .param("inviteCode", inviteCode)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode dataNode = root.get("data");
        GroupResponse response = objectMapper.treeToValue(dataNode, GroupResponse.class);

        assertThat(response.getId()).isEqualTo(createdGroup.getId());
        assertThat(response.getName()).isEqualTo("테스트 그룹");
        assertThat(response.getInviteCode()).isEqualTo(inviteCode);
        assertThat(response.getMaxParticipants()).isEqualTo(5);
    }

    @Test
    @DisplayName("POST /api/v1/groups/join - 그룹 참가 성공")
    void joinGroups() throws Exception {
        // Given
        // ✅ 그룹 생성 (user0 소유)
        GroupDTO createdGroup = groupService.createGroup(
                new CreateGroupCommand(
                        user0.getId(),
                        user0.getName(),
                        "profile.png",
                        "테스트 그룹",
                        5
                )
        );

        Long groupId = createdGroup.getId();

        // user1로 로그인 후 참가
        setSecurityContext(String.valueOf(user1.getId()));

        String nickname = "newMember";
        String profileImage = "2";

        JoinGroupRequest request = new JoinGroupRequest(
                createdGroup.getId(),
                nickname,
                profileImage
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(
                        post("/api/v1/groups/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode dataNode = root.get("data");
        GroupResponse response = objectMapper.treeToValue(dataNode, GroupResponse.class);

        assertThat(response.getId()).isEqualTo(groupId);
        assertThat(response.getName()).isEqualTo("테스트 그룹");
        assertThat(response.getMaxParticipants()).isEqualTo(5);

        List<GroupUserEntity> groupUsers = groupUserRepository.findByGroupId(groupId);

        assertThat(groupUsers.size()).isEqualTo(2);

        assertThat(groupUsers)
                .extracting(groupUser -> groupUser.getUser().getId())
                .containsExactlyInAnyOrder(user0.getId(), user1.getId());
    }

    void setSecurityContext(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // UserDetails 객체 생성
        Set<SimpleGrantedAuthority> roles = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new UserPrincipal(
                userId,
                "password",
                roles
        );

        // Authentication 생성
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

}
