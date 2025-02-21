package com.example.spring_Yunhyeok_01023567215.repository;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByOrderByIdAsc(Pageable pageable);
}
