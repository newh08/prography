package com.example.spring_Yunhyeok_01023567215.exception.user;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.USER_NOT_IN_ROOM;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class UserNotInRoomException extends BaseException {
    public UserNotInRoomException(String message) {
        super(message);
    }

    public UserNotInRoomException() {
        super(USER_NOT_IN_ROOM);
    }
}
