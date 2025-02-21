package com.example.spring_Yunhyeok_01023567215.controller;

import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.CreateRoomRequest;
import com.example.spring_Yunhyeok_01023567215.dto.RoomDetailResponse;
import com.example.spring_Yunhyeok_01023567215.dto.RoomListResponse;
import com.example.spring_Yunhyeok_01023567215.dto.UserIdRequest;
import com.example.spring_Yunhyeok_01023567215.service.RoomScheduler;
import com.example.spring_Yunhyeok_01023567215.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "Room API", description = "방(Room) 관련 API")
public class RoomController {

    private final RoomService roomService;
    private final RoomScheduler roomScheduler;

    @Operation(summary = "방 생성", description = "새로운 방을 생성합니다.")
    @PostMapping
    public ApiResponse<?> createRoom(
            @Parameter(description = "방 생성 요청 정보") @RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @Operation(summary = "모든 방 조회", description = "페이지네이션을 적용하여 모든 방 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<RoomListResponse> getAllRooms(
            @Parameter(description = "페이지당 조회할 방 개수") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "조회할 페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page) {
        RoomListResponse roomPageResponse = roomService.getAllRooms(size, page);
        return ApiResponse.success(roomPageResponse);
    }

    @Operation(summary = "방 상세 조회", description = "특정 방의 상세 정보를 조회합니다.")
    @GetMapping("/{roomId}")
    public ApiResponse<RoomDetailResponse> getRoomDetail(
            @Parameter(description = "조회할 방의 ID") @PathVariable Integer roomId) {
        RoomDetailResponse roomDetail = roomService.getRoomDetail(roomId);
        if (roomDetail == null) {
            return ApiResponse.badRequest();
        }
        return ApiResponse.success(roomDetail);
    }

    @Operation(summary = "방 참여", description = "사용자가 특정 방에 참여합니다.")
    @PostMapping("/attention/{roomId}")
    public ApiResponse<?> joinRoom(
            @Parameter(description = "참여할 방의 ID") @PathVariable Integer roomId,
            @Parameter(description = "참여할 사용자 ID") @RequestBody UserIdRequest request) {
        int userId = request.getUserId();
        return roomService.joinRoom(userId, roomId);
    }

    @Operation(summary = "방 나가기", description = "사용자가 특정 방에서 나갑니다.")
    @PostMapping("/out/{roomId}")
    public ApiResponse<?> leaveRoom(
            @Parameter(description = "나갈 방의 ID") @PathVariable Integer roomId,
            @Parameter(description = "나가는 사용자 ID") @RequestBody UserIdRequest request) {
        int userId = request.getUserId();
        return roomService.leaveRoom(userId, roomId);
    }

    @Operation(summary = "게임 시작", description = "특정 방에서 게임을 시작합니다.")
    @PutMapping("/start/{roomId}")
    public ApiResponse<?> startGame(
            @Parameter(description = "게임을 시작할 방의 ID") @PathVariable Integer roomId,
            @Parameter(description = "게임을 시작할 사용자 ID")@RequestBody UserIdRequest request) {
        ApiResponse<?> apiResponse = roomService.startGame(roomId, request.getUserId());
        roomScheduler.scheduleRoomStartGame(roomId);
        return apiResponse;
    }
}
