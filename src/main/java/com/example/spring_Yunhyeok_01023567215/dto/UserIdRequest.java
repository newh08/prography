package com.example.spring_Yunhyeok_01023567215.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "유져 ID 요청 DTO")
@RequiredArgsConstructor
public class UserIdRequest {

    @Schema(description = "유져 ID")
    private final Integer userId;
}