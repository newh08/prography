package com.example.spring_Yunhyeok_01023567215.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.scheduling.TaskScheduler;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomSchedulerTest {

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private RoomService roomService;

    private RoomScheduler roomScheduler;
    private final long testGameEndDelay = 60000L;

    @BeforeEach
    void setUp() {
        roomScheduler = new RoomScheduler(taskScheduler, roomService, testGameEndDelay);
    }

    @Test
    void testScheduleRoomStartGame() {
        int roomId = 100;
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);

        when(taskScheduler.schedule(runnableCaptor.capture(), dateCaptor.capture()))
                .thenReturn(null);

        roomScheduler.scheduleRoomStartGame(roomId);

        Runnable scheduledTask = runnableCaptor.getValue();
        assertNotNull(scheduledTask, "스케줄된 Runnable이 null이면 안 됩니다.");

        Date scheduledDate = dateCaptor.getValue();
        assertNotNull(scheduledDate, "스케줄된 실행 시간이 null이면 안 됩니다.");
        assertTrue(scheduledDate.after(new Date()), "실행 시간은 현재 시간 이후여야 합니다.");

        scheduledTask.run();

        verify(roomService).setRoomToFinish(roomId);
    }
}

