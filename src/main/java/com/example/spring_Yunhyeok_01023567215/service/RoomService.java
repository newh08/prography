package com.example.spring_Yunhyeok_01023567215.service;

import static com.example.spring_Yunhyeok_01023567215.domain.User.UserStatus.ACTIVE;
import static com.example.spring_Yunhyeok_01023567215.util.DataFormatter.formatter;

import com.example.spring_Yunhyeok_01023567215.domain.Room;
import com.example.spring_Yunhyeok_01023567215.domain.Room.RoomStatus;
import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.CreateRoomRequest;
import com.example.spring_Yunhyeok_01023567215.dto.RoomDetailResponse;
import com.example.spring_Yunhyeok_01023567215.dto.RoomListResponse;
import com.example.spring_Yunhyeok_01023567215.dto.RoomResponse;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomAllTeamsFullException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomFullException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomHostNotMatch;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotFullException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotValidStatusException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomTeamImbalanceException;
import com.example.spring_Yunhyeok_01023567215.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserProvider userProvider;
    private final UserRoomService userRoomService;


    public Room findRoomByIdOrThrow(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

    public boolean isRoomFull(int roomId) {
        Room room = findRoomByIdOrThrow(roomId);
        int currentMemberCount = userRoomService.getCurrentMemberCount(roomId);
        return room.isRoomFull(currentMemberCount);
    }

    @Transactional
    public ApiResponse<?> createRoom(CreateRoomRequest request) {
        // 1. 사용자 조회
        User user = userProvider.findUserByIdOrThrow(request.getUserId());

        // 2. 사용자 상태 확인
        userProvider.validateUserInStatus(user, ACTIVE);

        // 3. 사용자가 이미 참여한 방이 있는지 확인
        userRoomService.validateUserNotInAnyRoom(user.getId());

        // 4. 방 생성
        Room room = Room.builder()
                .title(request.getTitle())
                .room_type(Room.RoomType.valueOf(request.getRoomType()))
                .status(Room.RoomStatus.WAIT)
                .host(user) // 호스트 설정
                .build();

        roomRepository.save(room);

        // 5. UserRoom 생성하여 호스트를 방에 추가
        userRoomService.addUserToRoom(user, room, UserRoom.Team.RED);

        return ApiResponse.success();
    }

    public RoomListResponse getAllRooms(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Room> roomPage = roomRepository.findAll(pageRequest);

        List<RoomResponse> roomList = roomPage.getContent().stream()
                .map(room -> RoomResponse.builder()
                        .id(room.getId())
                        .title(room.getTitle())
                        .hostId(room.getHost().getId())
                        .roomType(room.getRoom_type().name())
                        .status(room.getStatus().name()).build()
                )

                .collect(Collectors.toList());

        return RoomListResponse.builder()
                .totalElements(roomPage.getTotalElements())
                .totalPages(roomPage.getTotalPages())
                .roomList(roomList)
                .build();
    }

    public RoomDetailResponse getRoomDetail(Integer roomId) {
        Room room = findRoomByIdOrThrow(roomId);
        return RoomDetailResponse.builder()
                .id(roomId)
                .title(room.getTitle())
                .status(room.getStatus().name())
                .roomType(room.getRoom_type().name())
                .hostId(room.getHost().getId())
                .createdAt(room.getCreated_at().format(formatter))
                .updatedAt(room.getUpdated_at().format(formatter))
                .build();
    }

    @Transactional
    public ApiResponse<?> leaveRoom(Integer userId, Integer roomId) {

        Room room = findRoomByIdOrThrow(roomId);

        // 사용자가 해당 방에 참가하고 있는지 확인
        UserRoom userRoom = userRoomService.validateUserParticipation(userId, roomId);

        // 방의 상태가 진행(PROGRESS) 중이거나 끝난(FINISH) 상태라면 나갈 수 없음
        if (room.getStatus() == RoomStatus.PROGRESS || room.getStatus() == RoomStatus.FINISH) {
            return ApiResponse.badRequest();
        }

        // 호스트가 나가는 경우
        if (room.getHost().getId().equals(userId)) {
            // 방에 속한 모든 사용자 제거
            userRoomService.removeAllUsersFromRoomId(room.getId());
            room.setStatus(RoomStatus.FINISH);
            roomRepository.save(room);
            return ApiResponse.success();
        }

        // 일반 사용자가 나갈 경우, 해당 UserRoom 정보 삭제
        userRoomService.removeUserRoom(userRoom);

        return ApiResponse.success();
    }



    @Transactional
    public ApiResponse<?> joinRoom(Integer userId, Integer roomId) {
        User user = userProvider.findUserByIdOrThrow(userId);
        Room room = findRoomByIdOrThrow(roomId);

        // 방의 상태가 대기(WAIT) 상태인지 확인
        validateRoomStatus(room, RoomStatus.WAIT);

        // 유저의 상태가 활성(ACTIVE) 상태인지 확인
        userProvider.validateUserInStatus(user, ACTIVE);
        userRoomService.validateUserNotInAnyRoom(userId);

        // 방의 정원이 가득 찼는지 확인
        validateRoomIsNotFull(roomId);

        // 팀 배정
        Team assignedTeam = determineTeamForUser(room);
        if (assignedTeam == null) {
            return ApiResponse.badRequest();
        }

        // 유저를 방에 추가
        userRoomService.addUserToRoom(user, room, assignedTeam);

        return ApiResponse.success();
    }

    private void validateRoomIsNotFull(Integer roomId) {
        boolean roomFull = isRoomFull(roomId);
        if (roomFull) {
            throw new RoomFullException();
        }
    }


    @Transactional
    public ApiResponse<?> startGame(int roomId, int userId) {
        Room room = findRoomByIdOrThrow(roomId);

        validateRoomStartCondition(roomId, userId, room);
        room.setStatus(RoomStatus.PROGRESS);
        roomRepository.save(room);

        return ApiResponse.success();
    }

    public void validateRoomStatus(Room room, RoomStatus roomStatus) {
        if (!room.getStatus().equals(roomStatus)) {
            throw new RoomNotValidStatusException();
        }
    }

    @Transactional
    public void setRoomToFinish(int roomId) {
        Room room = findRoomByIdOrThrow(roomId);
        room.setStatus(RoomStatus.FINISH);
        userRoomService.removeAllUsersFromRoomId(roomId);
        roomRepository.save(room);
    }

    private void validateRoomStartCondition(int roomId, int userId, Room room) {
        if (!room.isHost(userId)) {
            throw new RoomHostNotMatch();
        }

        if (!isRoomFull(roomId)) {
            throw new RoomNotFullException();
        }

        if (!isTeamsBalanced(roomId)) {
            throw new RoomTeamImbalanceException();
        }

        validateRoomStatus(room,RoomStatus.WAIT);
    }

    private boolean isTeamsBalanced(Integer roomId) {
        Map<Team, Long> teamCounts = userRoomService.getTeamCountsForRoom(roomId);
        long redTeamCount = teamCounts.getOrDefault(Team.RED, 0L);
        long blueTeamCount = teamCounts.getOrDefault(Team.BLUE, 0L);
        return redTeamCount == blueTeamCount;
    }

    private Team determineTeamForUser(Room room) {
        // UserRoomService를 통해 팀별 인원 수를 한 번에 조회
        Map<Team, Long> teamCounts = userRoomService.getTeamCountsForRoom(room.getId());
        int maxTeamSize = room.getMaxTeamSize();

        long redTeamCount = teamCounts.getOrDefault(Team.RED, 0L);
        long blueTeamCount = teamCounts.getOrDefault(Team.BLUE, 0L);

        // 팀 배정 로직: 먼저 RED팀, 그 다음 BLUE팀 순서로 배정
        if (redTeamCount < maxTeamSize) {
            return Team.RED;
        } else if (blueTeamCount < maxTeamSize) {
            return Team.BLUE;
        } else {
            throw new RoomAllTeamsFullException();
        }
    }
}