package com.example.spring_Yunhyeok_01023567215.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "초기화 요청 DTO")
public class InitRequest {

    @JsonProperty("seed")
    @Schema(description = "초기화 시 사용할 시드 값")
    private int seed;

    @JsonProperty("quantity")
    @Schema(description = "생성할 데이터 개수")
    private int quantity;
}