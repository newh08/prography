package com.example.spring_Yunhyeok_01023567215.controller;

import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.UserIdRequest;
import com.example.spring_Yunhyeok_01023567215.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 변경 관련 API")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 변경", description = "사용자가 특정 방에서 팀을 변경합니다.")
    @PutMapping("/{roomId}")
    public ApiResponse<?> changeTeam(
            @Parameter(description = "팀을 변경할 방의 ID") @PathVariable Integer roomId,
            @Parameter(description = "팀을 변경할 사용자 ID") @RequestBody UserIdRequest request) {
        Integer userId = request.getUserId();
        return teamService.changeTeam(roomId, userId);
    }
}
