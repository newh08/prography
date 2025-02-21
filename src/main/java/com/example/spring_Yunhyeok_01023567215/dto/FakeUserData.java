package com.example.spring_Yunhyeok_01023567215.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "가짜 유져 데이터 DTO")
public class FakeUserData {

    @Schema(description = "유져 ID")
    private int id;

    @Schema(description = "유져 이름 (실제 저장 이름)")
    private String username;

    @Schema(description = "유져 이메일")
    private String email;
}