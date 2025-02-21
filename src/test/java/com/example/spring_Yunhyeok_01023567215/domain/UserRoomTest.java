package com.example.spring_Yunhyeok_01023567215.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserRoomTest {

    @Test
    void testConstructorWithUserAndRoom() {
        // 생성자(User, Room)를 사용한 객체 생성
        User user = new User();
        user.setId(3);
        user.setName("Constructor User");

        Room room = Room.builder()
                .id(3)
                .title("Constructor Room")
                .build();

        UserRoom userRoom = new UserRoom(user, room);
        assertEquals(user, userRoom.getUser(), "생성자에서 전달한 user가 올바르게 설정되어야 합니다.");
        assertEquals(room, userRoom.getRoom(), "생성자에서 전달한 room이 올바르게 설정되어야 합니다.");
    }

}