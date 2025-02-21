package com.example.spring_Yunhyeok_01023567215.service;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.User.UserStatus;
import com.example.spring_Yunhyeok_01023567215.dto.FakeUserData;
import com.example.spring_Yunhyeok_01023567215.dto.UserListResponse;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotInStatusException;
import com.example.spring_Yunhyeok_01023567215.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserProvider{

    private final UserRepository userRepository;

    public UserListResponse getUserListResponse(int size, int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByIdAsc(pageRequest);
        return new UserListResponse(
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getContent());
    }

    public User findUserByIdOrThrow(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean validateUserInStatus(User user, UserStatus userStatus) {
        boolean isInStatus = user.getStatus() == userStatus;
        if (!isInStatus) {
            throw new UserNotInStatusException();
        }
        return true;
    }

    @Transactional
    public void resetUsers(List<FakeUserData> fakeUserDataList) {
        for (FakeUserData fakeUserData : fakeUserDataList) {
            User user = User.builder()
                    .fakerId(fakeUserData.getId())
                    .name(fakeUserData.getUsername())
                    .email(fakeUserData.getEmail())
                    .build();

            // 사용자 상태 설정
            if (fakeUserData.getId() <= 30) {
                user.setStatus(User.UserStatus.ACTIVE);
            } else if (fakeUserData.getId() <= 60) {
                user.setStatus(User.UserStatus.WAIT);
            } else {
                user.setStatus(User.UserStatus.NON_ACTIVE);
            }

            userRepository.save(user);
        }
    }



}