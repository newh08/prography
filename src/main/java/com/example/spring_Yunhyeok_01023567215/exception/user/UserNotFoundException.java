package com.example.spring_Yunhyeok_01023567215.exception.user;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.USER_NOT_FOUND;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}
