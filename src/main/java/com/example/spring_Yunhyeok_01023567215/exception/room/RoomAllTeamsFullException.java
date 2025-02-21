package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_ALL_TEAM_FULL;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomAllTeamsFullException extends BaseException {
    public RoomAllTeamsFullException(String message) {
        super(message);
    }

    public RoomAllTeamsFullException() {
        super(ROOM_ALL_TEAM_FULL);
    }
}
