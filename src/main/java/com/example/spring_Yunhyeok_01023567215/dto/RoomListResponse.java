package com.example.spring_Yunhyeok_01023567215.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@Schema(description = "방 목록 응답 DTO")
public class RoomListResponse {

    @Schema(description = "총 방 개수")
    private final long totalElements;

    @Schema(description = "총 페이지 수")
    private final int totalPages;

    @Schema(description = "방 목록")
    private final List<RoomResponse> roomList;
}
