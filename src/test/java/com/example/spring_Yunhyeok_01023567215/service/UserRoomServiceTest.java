package com.example.spring_Yunhyeok_01023567215.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.spring_Yunhyeok_01023567215.domain.Room;
import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import com.example.spring_Yunhyeok_01023567215.exception.room.RoomNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.userRoom.UserAlreadyInRoomException;
import com.example.spring_Yunhyeok_01023567215.exception.userRoom.UserRoomNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotInRoomException;
import com.example.spring_Yunhyeok_01023567215.repository.RoomRepository;
import com.example.spring_Yunhyeok_01023567215.repository.UserRoomRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRoomServiceTest {

    @InjectMocks
    private UserRoomService userRoomService;

    @Mock
    private UserRoomRepository userRoomRepository;

    @Mock
    private RoomRepository roomRepository;

    @Test
    void testValidateUserNotInAnyRoom_whenUserHasNoRoom_thenPasses() {
        // given
        int userId = 1;
        when(userRoomRepository.existsByUserId(userId)).thenReturn(false);

        // when, then (예외가 발생하지 않음)
        assertDoesNotThrow(() -> userRoomService.validateUserNotInAnyRoom(userId));
    }

    @Test
    void testValidateUserNotInAnyRoom_whenUserAlreadyInRoom_thenThrowsException() {
        // given
        int userId = 1;
        when(userRoomRepository.existsByUserId(userId)).thenReturn(true);

        // when, then
        assertThrows(UserAlreadyInRoomException.class,
                () -> userRoomService.validateUserNotInAnyRoom(userId));
    }

    @Test
    void testValidateUserParticipation_whenUserRoomExists_thenReturnsUserRoom() {
        // given
        int userId = 1;
        int roomId = 100;
        UserRoom userRoom = UserRoom.builder().build();
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId))
                .thenReturn(Optional.of(userRoom));

        // when
        UserRoom result = userRoomService.validateUserParticipation(userId, roomId);

        // then
        assertNotNull(result);
        assertEquals(userRoom, result);
    }

    @Test
    void testValidateUserParticipation_whenUserRoomNotFound_thenThrowsException() {
        // given
        int userId = 1;
        int roomId = 100;
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserRoomNotFoundException.class,
                () -> userRoomService.validateUserParticipation(userId, roomId));
    }

    @Test
    void testGetCurrentMemberCount() {
        // given
        int roomId = 50;
        int count = 5;
        when(userRoomRepository.countByRoomId(roomId)).thenReturn(count);

        // when
        int result = userRoomService.getCurrentMemberCount(roomId);

        // then
        assertEquals(count, result);
    }

    @Test
    void testAddUserToRoom() {
        // given
        User user = User.builder().id(1).name("Alice").build();
        Room room = Room.builder().id(100).title("Test Room").build();
        Team team = Team.RED;
        UserRoom savedUserRoom = UserRoom.builder().user(user).room(room).team(team).build();

        when(userRoomRepository.save(any(UserRoom.class))).thenReturn(savedUserRoom);

        // when
        UserRoom result = userRoomService.addUserToRoom(user, room, team);

        // then
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(room, result.getRoom());
        assertEquals(team, result.getTeam());
    }

    @Test
    void testRemoveAllUsersFromRoomId() {
        // given
        int roomId = 100;

        // when
        userRoomService.removeAllUsersFromRoomId(roomId);

        // then
        verify(userRoomRepository, times(1)).deleteByRoomId(roomId);
    }

    @Test
    void testRemoveUserRoom() {
        // given
        UserRoom userRoom = UserRoom.builder().id(1).build();

        // when
        userRoomService.removeUserRoom(userRoom);

        // then
        verify(userRoomRepository, times(1)).delete(userRoom);
    }


    @Test
    void testGetTeamCountsForRoom() {
        // given
        int roomId = 400;
        UserRoom ur1 = UserRoom.builder().team(Team.RED).build();
        UserRoom ur2 = UserRoom.builder().team(Team.BLUE).build();
        UserRoom ur3 = UserRoom.builder().team(Team.RED).build();
        List<UserRoom> userRooms = Arrays.asList(ur1, ur2, ur3);
        when(userRoomRepository.findByRoomId(roomId)).thenReturn(userRooms);

        // when
        Map<Team, Long> teamCounts = userRoomService.getTeamCountsForRoom(roomId);

        // then
        assertEquals(2L, teamCounts.get(Team.RED));
        assertEquals(1L, teamCounts.get(Team.BLUE));
    }

    @Test
    void testGetUserTeamOrThrow_whenUserRoomExists_thenReturnsTeam() {
        // given
        int userId = 1;
        int roomId = 100;
        Team team = Team.BLUE;
        UserRoom userRoom = UserRoom.builder().team(team).build();
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId))
                .thenReturn(Optional.of(userRoom));

        // when
        Team result = userRoomService.getUserTeamOrThrow(userId, roomId);

        // then
        assertEquals(team, result);
    }

    @Test
    void testGetUserTeamOrThrow_whenUserRoomNotFound_thenThrowsException() {
        // given
        int userId = 1;
        int roomId = 100;
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(UserNotInRoomException.class,
                () -> userRoomService.getUserTeamOrThrow(userId, roomId));
    }

    @Test
    void testChangeUserTeam() {
        // given
        int userId = 1;
        int roomId = 100;
        Team initialTeam = Team.RED;
        Team newTeam = Team.BLUE;
        UserRoom userRoom = UserRoom.builder().team(initialTeam).build();
        when(userRoomRepository.findByUserIdAndRoomId(userId, roomId))
                .thenReturn(Optional.of(userRoom));
        when(userRoomRepository.save(any(UserRoom.class))).thenReturn(userRoom);

        // when
        userRoomService.changeUserTeam(userId, roomId, newTeam);

        // then
        // 팀이 변경되었는지 확인
        assertEquals(newTeam, userRoom.getTeam());
        verify(userRoomRepository, times(1)).save(userRoom);
    }
}
