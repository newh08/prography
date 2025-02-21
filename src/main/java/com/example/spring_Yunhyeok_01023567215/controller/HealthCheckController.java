package com.example.spring_Yunhyeok_01023567215.controller;


import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health Check", description = "서버 상태를 확인하는 API")
public class HealthCheckController {

    @Operation(summary = "서버 상태 확인", description = "애플리케이션이 정상 작동 중인지 확인합니다.")
    @GetMapping("/health")
    public ApiResponse<?> healthCheck() {
        return ApiResponse.success();
    }
}
