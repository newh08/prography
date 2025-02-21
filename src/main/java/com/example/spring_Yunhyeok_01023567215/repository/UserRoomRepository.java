package com.example.spring_Yunhyeok_01023567215.repository;

import com.example.spring_Yunhyeok_01023567215.domain.UserRoom;
import com.example.spring_Yunhyeok_01023567215.domain.UserRoom.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
    Optional<UserRoom> findByUserIdAndRoomId(Integer userId, Integer roomId);
    boolean existsByUserId(Integer userId);
    int countByRoomId(Integer roomId);
    List<UserRoom> findByRoomId(Integer roomId);
    void deleteByRoomId(Integer roomId);

    // 특정 roomId와 team을 가진 UserRoom의 개수 반환
    int countByRoomIdAndTeam(Integer roomId, Team team);

}
