package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_NOT_FULL;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomNotFullException extends BaseException {
    public RoomNotFullException(String message) {
        super(message);
    }

    public RoomNotFullException() {
        super(ROOM_NOT_FULL);
    }
}
