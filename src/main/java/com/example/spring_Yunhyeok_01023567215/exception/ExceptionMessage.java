package com.example.spring_Yunhyeok_01023567215.exception;

public enum ExceptionMessage {
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    USER_NOT_IN_ROOM("유저가 방에 참가하지 않았습니다."),
    USER_NOT_IN_STATUS("유저 상태 검증에 실패했습니다."),
    ROOM_NOT_FOUND("방을 찾을 수 없습니다."),
    ROOM_FULL("방에 참여 인원이 가득찼습니다."),
    ROOM_NOT_FULL("방의 인원이 가득차지 않아 게임 시작이 불가합니다."),
    ROOM_NOT_JOINABLE("방이 참여할 수 없는 상태입니다."),
    ROOM_NOT_STARTABLE("방이 시작할 수 없는 상태입니다."),
    ROOM_HOST_NOT_MATCH("방의 호스트가 아닙니다"),
    ROOM_ALL_TEAM_FULL("방에 모든 팀이 가득 차있습니다."),
    ROOM_NOT_VALID_STATUS("방 상태가 검증 상태와 일치하지 않습니다."),
    ROOM_TEAM_IMBALANCE("방에 팀 인원이 균등한 상태가 아닙니다."),
    USER_ALREADY_IN_ROOM("유저가 이미 참여한 방이 있습니다."),
    USER_ROOM_NOT_FOUND("유저가 참여한 방이 없습니다."),
    TEAM_CAPACITY_EXCEED("옮기려는 팀 인원이 꽉찼습니다.");



    private final String message;

    ExceptionMessage( String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
