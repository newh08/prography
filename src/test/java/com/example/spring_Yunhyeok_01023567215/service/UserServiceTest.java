package com.example.spring_Yunhyeok_01023567215.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.spring_Yunhyeok_01023567215.domain.User;
import com.example.spring_Yunhyeok_01023567215.domain.User.UserStatus;
import com.example.spring_Yunhyeok_01023567215.dto.FakeUserData;
import com.example.spring_Yunhyeok_01023567215.dto.UserListResponse;
import com.example.spring_Yunhyeok_01023567215.dto.UserDetailResponse;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotFoundException;
import com.example.spring_Yunhyeok_01023567215.exception.user.UserNotInStatusException;
import com.example.spring_Yunhyeok_01023567215.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetUserListResponse() {
        List<User> users = Arrays.asList(
                User.builder().id(1).name("YH").status(User.UserStatus.ACTIVE).build(),
                User.builder().id(2).name("CHOI").status(User.UserStatus.ACTIVE).build()
        );
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(users, pageRequest, users.size());
        when(userRepository.findAllByOrderByIdAsc(any(PageRequest.class))).thenReturn(userPage);

        // when
        UserListResponse response = userService.getUserListResponse(10, 0);

        // then: 전체 요소 수와 페이지 수 검증
        assertEquals(users.size(), response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        // 각 DTO(UserResponse)의 필드(id, name, status 등)를 예상 값과 비교
        List<UserDetailResponse> userDetailRespons = response.getUserList();
        assertEquals(users.size(), userDetailRespons.size());

        for (int i = 0; i < users.size(); i++) {
            User expected = users.get(i);
            UserDetailResponse actual = userDetailRespons.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
            // 필요한 경우 상태나 다른 필드도 비교
            assertEquals(expected.getStatus().name(), actual.getStatus());
        }
    }


    @Test
    void testFindUserByIdOrThrow_UserFound() {
        // given: 존재하는 사용자 모의 객체
        User user = User.builder().id(1).name("Test User").build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.findUserByIdOrThrow(1);

        // then
        assertEquals(user, foundUser);
    }

    @Test
    void testFindUserByIdOrThrow_UserNotFound() {
        // given: 사용자 없음
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // then: 예외 발생 확인
        assertThrows(UserNotFoundException.class, () -> userService.findUserByIdOrThrow(1));
    }

    @Test
    void testValidateUserInStatus_ValidStatus() {
        // given
        User user = User.builder().id(1).name("Status Test").status(UserStatus.ACTIVE).build();

        // when & then: 상태가 ACTIVE이면 true 반환
        boolean result = userService.validateUserInStatus(user, UserStatus.ACTIVE);
        assertTrue(result);
    }

    @Test
    void testValidateUserInStatus_InvalidStatus() {
        // given
        User user = User.builder().id(2).name("Status Test").status(UserStatus.NON_ACTIVE).build();

        // then: 상태가 ACTIVE가 아니므로 예외 발생
        assertThrows(UserNotInStatusException.class, () -> userService.validateUserInStatus(user, UserStatus.ACTIVE));
    }

    @Test
    void testResetUsers() {
        // given: UserData 리스트를 생성 (ID 값에 따라 상태가 결정됨)
        FakeUserData fakeUserData1 = new FakeUserData();
        fakeUserData1.setId(10); // ACTIVE (id <= 30)
        fakeUserData1.setUsername("User10");
        fakeUserData1.setEmail("user10@example.com");

        FakeUserData fakeUserData2 = new FakeUserData();
        fakeUserData2.setId(40); // WAIT (id <= 60)
        fakeUserData2.setUsername("User40");
        fakeUserData2.setEmail("user40@example.com");

        FakeUserData fakeUserData3 = new FakeUserData();
        fakeUserData3.setId(70); // NON_ACTIVE (id > 60)
        fakeUserData3.setUsername("User70");
        fakeUserData3.setEmail("user70@example.com");

        List<FakeUserData> fakeUserDataList = Arrays.asList(fakeUserData1, fakeUserData2, fakeUserData3);

        // when: resetUsers 호출
        userService.resetUsers(fakeUserDataList);

        // then: repository.save가 3번 호출되었고, 각각의 User에 대해 올바른 상태가 설정되었는지 확인
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(3)).save(userCaptor.capture());
        List<User> savedUsers = userCaptor.getAllValues();

        // UserData의 입력 순서대로 상태가 할당되는지 검증
        assertEquals(UserStatus.ACTIVE, savedUsers.get(0).getStatus(), "id 10인 사용자는 ACTIVE 상태여야 합니다.");
        assertEquals(UserStatus.WAIT, savedUsers.get(1).getStatus(), "id 40인 사용자는 WAIT 상태여야 합니다.");
        assertEquals(UserStatus.NON_ACTIVE, savedUsers.get(2).getStatus(), "id 70인 사용자는 NON_ACTIVE 상태여야 합니다.");
    }
}
