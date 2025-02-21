package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_NOT_STARTABLE;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomNotStartableException extends BaseException {
    public RoomNotStartableException(String message) {
        super(message);
    }

    public RoomNotStartableException() {
        super(ROOM_NOT_STARTABLE);
    }
}
