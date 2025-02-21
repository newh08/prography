package com.example.spring_Yunhyeok_01023567215.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class RoomScheduler {

    private final TaskScheduler taskScheduler;
    private final RoomService roomService;
    private final long gameEndDelay;


    public RoomScheduler(TaskScheduler taskScheduler, RoomService roomService,
                         @Value("${game.end.delay}") long gameEndDelay) {
        this.taskScheduler = taskScheduler;
        this.roomService = roomService;
        this.gameEndDelay = gameEndDelay;
    }

    public void scheduleRoomStartGame(int roomId) {
        Date runAt = new Date(System.currentTimeMillis() + gameEndDelay);
        taskScheduler.schedule(() ->
                roomService.setRoomToFinish(roomId), runAt);
    }
}