package com.example.spring_Yunhyeok_01023567215.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@Schema(description = "방 응답 DTO")
public class RoomResponse {

    @Schema(description = "방 ID")
    private final int id;

    @Schema(description = "방 제목")
    private final String title;

    @Schema(description = "방장(호스트)의 유져 ID")
    private final int hostId;

    @Schema(description = "방 타입")
    private final String roomType;

    @Schema(description = "방 상태")
    private final String status;
}
