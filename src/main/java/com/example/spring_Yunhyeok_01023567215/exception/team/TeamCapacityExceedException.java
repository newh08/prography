package com.example.spring_Yunhyeok_01023567215.exception.team;

import static com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage.TEAM_CAPACITY_EXCEED;

import com.example.spring_Yunhyeok_01023567215.exception.BaseException;
import com.example.spring_Yunhyeok_01023567215.exception.ExceptionMessage;

public class TeamCapacityExceedException extends BaseException {
    public TeamCapacityExceedException(String message) {
        super(message);
    }

    public TeamCapacityExceedException() {
        super(TEAM_CAPACITY_EXCEED);
    }
}
