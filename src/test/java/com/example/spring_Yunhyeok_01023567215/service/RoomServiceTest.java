package com.example.spring_Yunhyeok_01023567215.service;

import static com.example.spring_Yunhyeok_01023567215.domain.User.UserStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.spring_Yunhyeok_01023567215.domain.Room;
import com.example.spring_Yunhyeok_01023567215.domain.Room.RoomStatus;
import com.example.spring_Yunhyeok_01023567215.domain.Room.RoomType;
import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.CreateRoomRequest;
import com.example.spring_Yunhyeok_01023567215.dto.RoomDetailResponse;
import com.example.spring_Yunhyeok_01023567215.dto.RoomListResponse;
import com.example.spring_Yunhyeok_01023567215.dto.RoomResponse;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomFullException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomHostNotMatch;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotFullException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomTeamImbalanceException;
import com.example.spring_Yunhyeok_01023567215.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserProvider userProvider;

    @Mock
    private UserRoomService userRoomService;

    private User hostUser;
    private Room room;

    @BeforeEach
    public void setUp() {
        hostUser = User.builder().id(1).name("Host").build();
        room = Room.builder()
                .id(100)
                .title("Test Room")
                .room_type(RoomType.DOUBLE)
                .status(RoomStatus.WAIT)
                .host(hostUser)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindRoomByIdOrThrow_Found() {
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        Room found = roomService.findRoomByIdOrThrow(100);
        assertEquals(room, found);
    }

    @Test
    void testFindRoomByIdOrThrow_NotFound() {
        when(roomRepository.findById(100)).thenReturn(Optional.empty());
        assertThrows(RoomNotFoundException.class, () -> roomService.findRoomByIdOrThrow(100));
    }

    @Test
    void testIsRoomFull() {
        // GIVEN: room와 userRoomService.getCurrentMemberCount가 반환하는 인원 수에 따라
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        // DOUBLE 방의 최대 인원은 4. 4명이 참여하면 꽉 찬 것으로 판단.
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(4);
        assertTrue(roomService.isRoomFull(100));
    }

    @Test
    void testCreateRoom_Success() {
        // given
        CreateRoomRequest request = new CreateRoomRequest();
        request.setUserId(1);
        request.setTitle("New Room");
        request.setRoomType("DOUBLE");

        // 사용자 조회 및 상태 검증
        when(userProvider.findUserByIdOrThrow(1)).thenReturn(hostUser);
        // validateUserInStatus가 boolean을 반환하므로 doNothing() 대신 when().thenReturn(true)
        when(userProvider.validateUserInStatus(any(User.class), any())).thenReturn(true);
        doNothing().when(userRoomService).validateUserNotInAnyRoom(1);

        // 방 저장 시 id 할당
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> {
            Room r = invocation.getArgument(0);
            r.setId(101);
            return r;
        });

        // 호스트를 방에 추가
        UserRoom userRoom = UserRoom.builder().user(hostUser).room(room).team(Team.RED).build();
        when(userRoomService.addUserToRoom(eq(hostUser), any(Room.class), eq(Team.RED)))
                .thenReturn(userRoom);

        // when
        ApiResponse<?> response = roomService.createRoom(request);

        // then
        assertEquals(200, response.getCode());
        verify(userProvider).findUserByIdOrThrow(1);
        verify(userProvider).validateUserInStatus(hostUser, ACTIVE);
        verify(userRoomService).validateUserNotInAnyRoom(1);
        verify(roomRepository).save(any(Room.class));
        verify(userRoomService).addUserToRoom(eq(hostUser), any(Room.class), eq(Team.RED));
    }


    @Test
    void testGetAllRooms() {
        // given
        Room anotherRoom = Room.builder()
                .id(102)
                .title("Room 102")
                .host(hostUser)
                .room_type(RoomType.SINGLE)
                .status(RoomStatus.WAIT)
                .build();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Room> roomPage = new PageImpl<>(Arrays.asList(room, anotherRoom), pageRequest, 2);
        when(roomRepository.findAll(any(PageRequest.class))).thenReturn(roomPage);

        // when
        RoomListResponse response = roomService.getAllRooms(10, 0);

        // then
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        // RoomResponse의 필드 비교
        RoomResponse firstResponse = response.getRoomList().get(0);
        assertEquals(room.getId(), firstResponse.getId());
        assertEquals(room.getTitle(), firstResponse.getTitle());
        assertEquals(hostUser.getId(), firstResponse.getHostId());
        assertEquals(room.getRoom_type().name(), firstResponse.getRoomType());
        assertEquals(room.getStatus().name(), firstResponse.getStatus());
    }

    @Test
    void testGetRoomDetail() {
        // given
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));

        // when
        RoomDetailResponse detail = roomService.getRoomDetail(100);

        // then
        assertEquals(room.getId(), detail.getId());
        assertEquals(room.getTitle(), detail.getTitle());
        assertEquals(room.getStatus().name(), detail.getStatus());
        assertEquals(room.getRoom_type().name(), detail.getRoomType());
        assertEquals(hostUser.getId(), detail.getHostId());
        assertNotNull(detail.getCreatedAt());
        assertNotNull(detail.getUpdatedAt());
    }

    @Test
    void testLeaveRoom_HostLeaving() {
        // Host가 나갈 경우: 전체 사용자 삭제 후 방 상태 FINISH
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        UserRoom userRoom = UserRoom.builder().user(hostUser).room(room).build();
        when(userRoomService.validateUserParticipation(hostUser.getId(), 100)).thenReturn(userRoom);

        ApiResponse<?> response = roomService.leaveRoom(hostUser.getId(), 100);
        assertEquals(200, response.getCode());
        verify(userRoomService).removeAllUsersFromRoomId(100);
        verify(roomRepository).save(room);
        assertEquals(RoomStatus.FINISH, room.getStatus());
    }

    @Test
    void testLeaveRoom_NonHostLeaving() {
        // 일반 사용자가 나갈 경우: 해당 UserRoom만 삭제
        User nonHost = User.builder().id(2).name("NonHost").build();
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        UserRoom userRoom = UserRoom.builder().user(nonHost).room(room).build();
        when(userRoomService.validateUserParticipation(nonHost.getId(), 100)).thenReturn(userRoom);

        ApiResponse<?> response = roomService.leaveRoom(nonHost.getId(), 100);
        assertEquals(200, response.getCode());
        verify(userRoomService).removeUserRoom(userRoom);
    }

    @Test
    void testLeaveRoom_InvalidStatus() {
        // 방 상태가 PROGRESS일 때 나갈 수 없음
        room.setStatus(RoomStatus.PROGRESS);
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        UserRoom userRoom = UserRoom.builder().user(hostUser).room(room).build();
        when(userRoomService.validateUserParticipation(hostUser.getId(), 100)).thenReturn(userRoom);

        ApiResponse<?> response = roomService.leaveRoom(hostUser.getId(), 100);
        assertEquals(201, response.getCode());
    }

    @Test
    void testJoinRoom_Success() {
        // given
        User joiningUser = User.builder().id(2).name("Joiner").build();
        when(userProvider.findUserByIdOrThrow(2)).thenReturn(joiningUser);
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        // validateUserInStatus가 boolean을 반환하므로 doNothing() 대신 when().thenReturn(true)
        when(userProvider.validateUserInStatus(joiningUser, ACTIVE)).thenReturn(true);
        doNothing().when(userRoomService).validateUserNotInAnyRoom(2);
        // 방 정원 체크: 현재 인원 1 (방이 가득 차지 않은 상태)
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(1);
        // 팀 배정: 두 팀 모두 0명인 상태
        Map<Team, Long> teamCounts = new HashMap<>();
        teamCounts.put(Team.RED, 0L);
        teamCounts.put(Team.BLUE, 0L);
        when(userRoomService.getTeamCountsForRoom(100)).thenReturn(teamCounts);
        // 유저를 방에 추가
        UserRoom newUserRoom = UserRoom.builder().user(joiningUser).room(room).team(Team.RED).build();
        when(userRoomService.addUserToRoom(joiningUser, room, Team.RED)).thenReturn(newUserRoom);

        // when
        ApiResponse<?> response = roomService.joinRoom(2, 100);

        // then
        assertEquals(200, response.getCode());
    }


    @Test
    void testJoinRoom_RoomFullException() {
        // given: 방 정원이 가득 찬 경우
        User joiningUser = User.builder().id(2).name("Joiner").build();
        when(userProvider.findUserByIdOrThrow(2)).thenReturn(joiningUser);
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        // validateUserInStatus가 boolean을 반환하므로 doNothing() 대신 when().thenReturn(true)
        when(userProvider.validateUserInStatus(joiningUser, ACTIVE)).thenReturn(true);
        // void 메서드인 validateUserNotInAnyRoom는 doNothing() 사용 가능
        doNothing().when(userRoomService).validateUserNotInAnyRoom(2);
        // 현재 인원이 최대치(4)라고 가정
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(4);

        // when, then
        assertThrows(RoomFullException.class, () -> roomService.joinRoom(2, 100));
    }


    @Test
    void testStartGame_Success() {
        // given: 게임 시작 조건 충족 (호스트, 방이 꽉 찼고, 팀 균형)
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(4);
        Map<Team, Long> teamCounts = new HashMap<>();
        teamCounts.put(Team.RED, 2L);
        teamCounts.put(Team.BLUE, 2L);
        when(userRoomService.getTeamCountsForRoom(100)).thenReturn(teamCounts);
        room.setStatus(RoomStatus.WAIT);

        ApiResponse<?> response = roomService.startGame(100, hostUser.getId());
        assertEquals(200, response.getCode());
        assertEquals(RoomStatus.PROGRESS, room.getStatus());
        verify(roomRepository).save(room);
    }

    @Test
    void testStartGame_RoomHostNotMatch() {
        // given: 호스트가 아닌 사용자가 게임 시작 시도
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        assertThrows(RoomHostNotMatch.class, () -> roomService.startGame(100, 2));
    }

    @Test
    void testStartGame_RoomNotFull() {
        // given: 방이 가득 차지 않은 경우
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(3);
        assertThrows(RoomNotFullException.class, () -> roomService.startGame(100, hostUser.getId()));
    }

    @Test
    void testStartGame_TeamImbalance() {
        // given: 방은 꽉 찼지만 팀 구성 불균형
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        when(userRoomService.getCurrentMemberCount(100)).thenReturn(4);
        Map<Team, Long> teamCounts = new HashMap<>();
        teamCounts.put(Team.RED, 3L);
        teamCounts.put(Team.BLUE, 1L);
        when(userRoomService.getTeamCountsForRoom(100)).thenReturn(teamCounts);
        assertThrows(RoomTeamImbalanceException.class, () -> roomService.startGame(100, hostUser.getId()));
    }

    @Test
    void testSetRoomToFinish() {
        // given: 방 종료 처리
        when(roomRepository.findById(100)).thenReturn(Optional.of(room));
        doNothing().when(userRoomService).removeAllUsersFromRoomId(100);
        when(roomRepository.save(room)).thenReturn(room);

        roomService.setRoomToFinish(100);
        assertEquals(RoomStatus.FINISH, room.getStatus());
        verify(userRoomService).removeAllUsersFromRoomId(100);
        verify(roomRepository).save(room);
    }
}
