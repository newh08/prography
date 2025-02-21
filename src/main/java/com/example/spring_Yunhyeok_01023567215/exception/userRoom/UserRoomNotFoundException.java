package com.example.spring_Yunhyeok_01023567215.exception.userRoom;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.USER_ROOM_NOT_FOUND;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;
import java.util.Map;

public class UserRoomNotFoundException extends BaseException {
    public UserRoomNotFoundException(String message) {
        super(message);
    }

    public UserRoomNotFoundException(int userId, int roomId) {
        super(USER_ROOM_NOT_FOUND, Map.of(
                "userId", userId,
                "roomId", roomId
        ));
    }
}
