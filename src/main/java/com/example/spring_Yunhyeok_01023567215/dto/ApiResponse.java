package com.example.spring_Yunhyeok_01023567215.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@Schema(description = "API 응답 객체")
public class ApiResponse<T> {

    @Schema(description = "응답 코드 (예: 200, 201, 500)")
    private final int code;

    @Schema(description = "응답 메시지")
    private final String message;

    @Schema(description = "API 응답 데이터")
    private final T result;

    public ApiResponse(int code, String message) {
        this(code, message, null);
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data) {

        return new ApiResponse<>(200, "API 요청이 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<T>(200, "API 요청이 성공했습니다.");
    }

    // 잘못된 요청 응답 생성 메서드
    public static <T> ApiResponse<T> badRequest() {
        return new ApiResponse<>(201, "불가능한 요청입니다.", null);
    }

    // 서버 에러 응답 생성 메서드
    public static <T> ApiResponse<T> serverError() {
        return new ApiResponse<>(500, "에러가 발생했습니다.", null);
    }

}