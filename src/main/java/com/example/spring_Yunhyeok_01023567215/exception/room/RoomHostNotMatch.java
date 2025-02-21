package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_HOST_NOT_MATCH;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomHostNotMatch extends BaseException {
    public RoomHostNotMatch(String message) {
        super(message);
    }

    public RoomHostNotMatch() {
        super(ROOM_HOST_NOT_MATCH);
    }
}
