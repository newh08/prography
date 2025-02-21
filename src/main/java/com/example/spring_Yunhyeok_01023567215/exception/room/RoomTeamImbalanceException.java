package com.example.spring_Yunhyeok_01023567215.exception.room;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.ROOM_TEAM_IMBALANCE;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class RoomTeamImbalanceException extends BaseException {
    public RoomTeamImbalanceException(String message) {
        super(message);
    }

    public RoomTeamImbalanceException() {
        super(ROOM_TEAM_IMBALANCE);
    }
}
