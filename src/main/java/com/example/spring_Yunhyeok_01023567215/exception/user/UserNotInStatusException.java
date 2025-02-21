package com.example.spring_Yunhyeok_01023567215.exception.user;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.USER_NOT_IN_STATUS;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class UserNotInStatusException extends BaseException{
    public UserNotInStatusException(String message) {
        super(message);
    }

    public UserNotInStatusException() {
        super(USER_NOT_IN_STATUS);
    }
}
