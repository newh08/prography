package com.example.spring_Yunhyeok_01023567215.exception.userRoom;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.USER_ALREADY_IN_ROOM;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class UserAlreadyInRoomException extends BaseException {
    public UserAlreadyInRoomException(String message) {
        super(message);
    }

    public UserAlreadyInRoomException() {
        super(USER_ALREADY_IN_ROOM);
    }
}
