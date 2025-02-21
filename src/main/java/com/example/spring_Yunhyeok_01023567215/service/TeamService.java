package com.example.spring_Yunhyeok_01023567215.service;

import com.example.spring_Yunhyeok_01023567215.domain.Room;
import com.example.spring_Yunhyeok_01023567215.domain.Room.RoomStatus;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.exception.team.TeamCapacityExceedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final RoomService roomService;
    private final UserRoomService userRoomService;

    @Transactional
    public ApiResponse<?> changeTeam(Integer roomId, Integer userId) {
        Room room = roomService.findRoomByIdOrThrow(roomId);

        roomService.validateRoomStatus(room,RoomStatus.WAIT);

        userRoomService.validateUserParticipation(userId, roomId);

        Team currentTeam = userRoomService.getUserTeamOrThrow(userId, roomId);
        Team newTeam = (currentTeam == Team.RED) ? Team.BLUE : Team.RED;

        if (userRoomService.isTeamFull(room, newTeam)) {
            throw new TeamCapacityExceedException();
        }

        userRoomService.changeUserTeam(userId, roomId, newTeam);

        return ApiResponse.success();
    }

}
