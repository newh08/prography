package com.example.spring_Yunhyeok_01023567215.service;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.User.UserStatus;

public interface UserProvider {
    User findUserByIdOrThrow(int userId);
    boolean validateUserInStatus(User user, UserStatus userStatus);
}
