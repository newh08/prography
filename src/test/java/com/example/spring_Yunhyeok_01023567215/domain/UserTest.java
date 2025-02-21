package com.example.spring_Yunhyeok_01023567215.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testDefaultTimestamps() {
        // 빌더를 이용하여 User 객체 생성 시, created_at과 updated_at 필드의 디폴트 값이 올바르게 설정되는지 확인
        User user = User.builder()
                .name("Timestamp Test")
                .build();

        LocalDateTime createdAt = user.getCreated_at();
        LocalDateTime updatedAt = user.getUpdated_at();

        assertNotNull(createdAt, "created_at 필드는 null 이 아니어야 합니다.");
        assertNotNull(updatedAt, "updated_at 필드는 null 이 아니어야 합니다.");

    }

}