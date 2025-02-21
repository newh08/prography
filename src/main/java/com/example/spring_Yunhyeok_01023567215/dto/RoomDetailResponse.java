package com.example.spring_Yunhyeok_01023567215.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@Builder
@Schema(description = "방 상세 정보 응답 DTO")
public class RoomDetailResponse {

    @Schema(description = "방 ID")
    private final Integer id;

    @Schema(description = "방 제목")
    private final String title;

    @Schema(description = "방장(호스트)의 우져 ID")
    private final Integer hostId;

    @Schema(description = "방 타입")
    private final String roomType;

    @Schema(description = "방의 현재 상태")
    private final String status;

    @Schema(description = "방 생성 시간")
    private final String createdAt;

    @Schema(description = "방 마지막 업데이트 시간")
    private final String updatedAt;
}
