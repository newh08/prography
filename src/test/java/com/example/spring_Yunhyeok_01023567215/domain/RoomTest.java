package com.example.spring_Yunhyeok_01023567215.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RoomTest {
    @Test
    void testGetMaxTeamSize() {
        // SINGLE: 최대 플레이어 2 -> 팀 당 1명
        Room roomSingle = Room.builder()
                .room_type(Room.RoomType.SINGLE)
                .build();
        assertEquals(1, roomSingle.getMaxTeamSize(), "SINGLE 타입 방은 팀 사이즈가 1이어야 합니다.");

        // DOUBLE: 최대 플레이어 4 -> 팀 당 2명
        Room roomDouble = Room.builder()
                .room_type(Room.RoomType.DOUBLE)
                .build();
        assertEquals(2, roomDouble.getMaxTeamSize(), "DOUBLE 타입 방은 팀 사이즈가 2여야 합니다.");
    }

    @Test
    void testGetMaxCapacity() {
        // SINGLE: 최대 플레이어 2
        Room roomSingle = Room.builder()
                .room_type(Room.RoomType.SINGLE)
                .build();
        assertEquals(2, roomSingle.getMaxCapacity(), "SINGLE 타입 방의 최대 인원은 2명이어야 합니다.");

        // DOUBLE: 최대 플레이어 4
        Room roomDouble = Room.builder()
                .room_type(Room.RoomType.DOUBLE)
                .build();
        assertEquals(4, roomDouble.getMaxCapacity(), "DOUBLE 타입 방의 최대 인원은 4명이어야 합니다.");
    }

    @Test
    void testIsRoomFull() {
        Room room = Room.builder()
                .room_type(Room.RoomType.DOUBLE) // 최대 인원 4
                .build();
        // 현재 인원이 3이면 꽉 차지 않은 상태
        assertFalse(room.isRoomFull(3), "현재 인원이 3명인 경우 방은 꽉 차지 않았어야 합니다.");
        // 4명 이상이면 꽉 찬 상태
        assertTrue(room.isRoomFull(4), "현재 인원이 4명인 경우 방은 꽉 찼어야 합니다.");
        assertTrue(room.isRoomFull(5), "현재 인원이 5명인 경우 방은 꽉 찼어야 합니다.");
    }

    @Test
    void testIsHost() {
        User host = new User();
        host.setId(1);
        Room room = Room.builder()
                .host(host)
                .build();

        assertTrue(room.isHost(1), "ID 1인 경우 호스트여야 합니다.");
        assertFalse(room.isHost(2), "ID 2인 경우 호스트가 아니어야 합니다.");
    }

    @Test
    void testRoomStatusIsFinish() {
        // FINISH 상태의 경우 isFinish()는 true여야 합니다.
        assertTrue(Room.RoomStatus.FINISH.isFinish(), "FINISH 상태의 isFinish()는 true여야 합니다.");
        // WAIT, PROGRESS 상태의 경우 false여야 합니다.
        assertFalse(Room.RoomStatus.WAIT.isFinish(), "WAIT 상태의 isFinish()는 false여야 합니다.");
        assertFalse(Room.RoomStatus.PROGRESS.isFinish(), "PROGRESS 상태의 isFinish()는 false여야 합니다.");
    }
}