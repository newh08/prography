package com.example.spring_Yunhyeok_01023567215.controller;

import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.UserListResponse;
import com.example.spring_Yunhyeok_01023567215.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관련 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 목록 조회", description = "페이지네이션을 적용하여 사용자 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<UserListResponse> getUsers(
            @Parameter(description = "페이지당 조회할 사용자 수")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "조회할 페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page
    ) {
        UserListResponse response = userService.getUserListResponse(size, page);
        return ApiResponse.success(response);
    }
}
