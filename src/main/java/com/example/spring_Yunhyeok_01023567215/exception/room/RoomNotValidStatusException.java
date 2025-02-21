package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_NOT_VALID_STATUS;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomNotValidStatusException extends BaseException {

    public RoomNotValidStatusException(String message) {
        super(message);
    }

    public RoomNotValidStatusException() {
        super(ROOM_NOT_VALID_STATUS);
    }
}
