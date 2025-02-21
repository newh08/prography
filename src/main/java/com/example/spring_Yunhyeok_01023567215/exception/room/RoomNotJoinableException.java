package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_NOT_JOINABLE;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;

public class RoomNotJoinableException extends BaseException {
    public RoomNotJoinableException(String message) {
        super(message);
    }

    public RoomNotJoinableException() {
        super(ROOM_NOT_JOINABLE);
    }
}
