package com.example.spring_Yunhyeok_01023567215.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "방 생성 요청 DTO")
public class CreateRoomRequest {

    @Schema(description = "유져 ID")
    private Integer userId;

    @Schema(description = "방 타입")
    private String roomType;

    @Schema(description = "방 제목")
    private String title;
}