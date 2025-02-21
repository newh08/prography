package com.example.spring_Yunhyeok_01023567215.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "유져 초기화 API 응답 DTO")
public class UserResetApiResponse {

    @Schema(description = "응답 상태")
    private final String status;

    @Schema(description = "유져 데이터 목록")
    private final List<FakeUserData> data;
}