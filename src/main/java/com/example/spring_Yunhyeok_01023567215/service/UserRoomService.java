package com.example.spring_Yunhyeok_01023567215.service;

import com.example.spring_Yunhyeok_01023567215.domain.Room;
import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotInRoomException;
import com.example.spring_Yunhyeok_01023567215.exception.userRoom.UserAlreadyInRoomException;
import com.example.spring_Yunhyeok_01023567215.exception.userRoom.UserRoomNotFoundException;
import com.example.spring_Yunhyeok_01023567215.repository.UserRoomRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;

    public void validateUserNotInAnyRoom(int userId) {
        if (userRoomRepository.existsByUserId(userId)) {
            throw new UserAlreadyInRoomException();
        }
    }

    public UserRoom validateUserParticipation(int userId, int roomId) {
        return userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new UserRoomNotFoundException(userId, roomId));
    }

    public int getCurrentMemberCount(int roomId) {
        return userRoomRepository.countByRoomId(roomId);
    }

    public UserRoom addUserToRoom(User user, Room room, Team team) {
        UserRoom userRoom = UserRoom.builder()
                .room(room)
                .user(user)
                .team(team)
                .build();
        return userRoomRepository.save(userRoom);
    }

    // 호스트가 나갈 경우, 방에 속한 모든 사용자 삭제
    public void removeAllUsersFromRoomId(int roomId) {
        userRoomRepository.deleteByRoomId(roomId);
    }

    // 일반 사용자가 나갈 경우, 해당 UserRoom 삭제
    public void removeUserRoom(UserRoom userRoom) {
        userRoomRepository.delete(userRoom);
    }

    public boolean isTeamFull(Room room, Team team) {
        int teamCount = userRoomRepository.countByRoomIdAndTeam(room.getId(), team);
        int maxTeamSize = room.getMaxTeamSize();
        return teamCount >= maxTeamSize;
    }

    public Map<Team, Long> getTeamCountsForRoom(int roomId) {
        return userRoomRepository.findByRoomId(roomId)
                .stream()
                .collect(Collectors.groupingBy(UserRoom::getTeam, Collectors.counting()));
    }

    public Team getUserTeamOrThrow(int userId, int roomId) {
        UserRoom userRoom = userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(UserNotInRoomException::new);
        return userRoom.getTeam();
    }

    @Transactional
    public void changeUserTeam(int userId, int roomId, Team newTeam) {
        UserRoom userRoom = userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(UserNotInRoomException::new);
        userRoom.setTeam(newTeam);
        userRoomRepository.save(userRoom);
    }

}
