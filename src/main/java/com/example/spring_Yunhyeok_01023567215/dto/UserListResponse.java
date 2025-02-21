package com.example.spring_Yunhyeok_01023567215.dto;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Schema(description = "유져 목록 응답 DTO")
public class UserListResponse {

    @Schema(description = "총 유져 수")
    private final long totalElements;

    @Schema(description = "총 페이지 수")
    private final int totalPages;

    @Schema(description = "유져 목록")
    private final List<UserDetailResponse> userList;

    public UserListResponse(long totalElements, int totalPages, List<User> users) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.userList = users.stream()
                .map(UserDetailResponse::new)
                .collect(Collectors.toList());
    }

    public static UserListResponse of(long totalElements, int totalPages, List<User> users) {
        return new UserListResponse(totalElements, totalPages, users);
    }
}