package com.example.spring_Yunhyeok_01023567215.controller;

import com.example.spring_Yunhyeok_01023567215.dto.*;
import com.example.spring_Yunhyeok_01023567215.service.RoomScheduler;
import com.example.spring_Yunhyeok_01023567215.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomScheduler roomScheduler;

    @InjectMocks
    private RoomController roomController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    @DisplayName("방 생성 API 테스트")
    void createRoomTest() throws Exception {
        CreateRoomRequest request = new CreateRoomRequest();
        when(roomService.createRoom(any(CreateRoomRequest.class)))
                .thenReturn(ApiResponse.success(null));

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("모든 방 조회 API 테스트")
    void getAllRoomsTest() throws Exception {
        RoomListResponse roomListResponse = new RoomListResponse(100, 10, List.of());
        when(roomService.getAllRooms(anyInt(), anyInt())).thenReturn(roomListResponse);

        mockMvc.perform(get("/room")
                        .param("size", "10")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.totalElements").value(100));
    }

    @Test
    @DisplayName("방 상세 조회 API 테스트")
    void getRoomDetailTest() throws Exception {
        RoomDetailResponse response = RoomDetailResponse.builder()
                .id(1)
                .title("테스트 방")
                .hostId(100)
                .status("OPEN")
                .createdAt("2024-02-21")
                .updatedAt("2024-02-21")
                .build();
        when(roomService.getRoomDetail(anyInt())).thenReturn(response);

        mockMvc.perform(get("/room/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.title").value("테스트 방"));
    }

    @Test
    @DisplayName("방 참여 API 테스트")
    void joinRoomTest() throws Exception {
        UserIdRequest request = new UserIdRequest(1);
        when(roomService.joinRoom(anyInt(), anyInt()))
                .thenReturn(ApiResponse.success(null));

        mockMvc.perform(post("/room/attention/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("방 나가기 API 테스트")
    void leaveRoomTest() throws Exception {
        UserIdRequest request = new UserIdRequest(1);
        when(roomService.leaveRoom(anyInt(), anyInt()))
                .thenReturn(ApiResponse.success(null));

        mockMvc.perform(post("/room/out/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("게임 시작 API 테스트")
    void startGameTest() throws Exception {
        UserIdRequest request = new UserIdRequest(1);
        when(roomService.startGame(anyInt(), anyInt()))
                .thenReturn(ApiResponse.success(null));
        doNothing().when(roomScheduler).scheduleRoomStartGame(anyInt());

        mockMvc.perform(put("/room/start/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        Mockito.verify(roomScheduler).scheduleRoomStartGame(1); // 스케줄러 실행 여부 확인
    }
}
