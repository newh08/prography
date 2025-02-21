package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_NOT_FOUND;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomNotFoundException extends BaseException
{
    public RoomNotFoundException(String message) {
        super(message);
    }

    public RoomNotFoundException() {
        super(ROOM_NOT_FOUND);
    }
}
