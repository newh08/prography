package com.example.spring_Yunhyeok_01023567215.dto;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
@Schema(description = "유져 상세 정보 응답 DTO")
public class UserDetailResponse {

    @Schema(description = "유져 ID")
    private final Integer id;

    @Schema(description = "Faker ID")
    private final Integer fakerId;

    @Schema(description = "유져 이름")
    private final String name;

    @Schema(description = "유져 이메일")
    private final String email;

    @Schema(description = "유져 상태 (예: ACTIVE, INACTIVE)")
    private final String status;

    @Schema(description = "유져 계정 생성 날짜")
    private final String createdAt;

    @Schema(description = "유져 정보 마지막 업데이트 날짜")
    private final String updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserDetailResponse(User user) {
        this.id = user.getId();
        this.fakerId = user.getFakerId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.status = user.getStatus().name();
        this.createdAt = user.getCreated_at().format(formatter);
        this.updatedAt = user.getUpdated_at().format(formatter);
    }
}