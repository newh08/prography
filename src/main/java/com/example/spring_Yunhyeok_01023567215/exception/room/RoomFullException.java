package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_FULL;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomFullException extends BaseException {
    public RoomFullException(String message) {
        super(message);
    }

    public RoomFullException() {
        super(ROOM_FULL);
    }
}
