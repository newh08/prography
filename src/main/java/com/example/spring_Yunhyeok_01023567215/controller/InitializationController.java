package com.example.spring_Yunhyeok_01023567215.controller;

import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import com.example.spring_Yunhyeok_01023567215.dto.InitRequest;
import com.example.spring_Yunhyeok_01023567215.dto.FakeUserData;
import com.example.spring_Yunhyeok_01023567215.dto.UserResetApiResponse;
import com.example.spring_Yunhyeok_01023567215.repository.RoomRepository;
import com.example.spring_Yunhyeok_01023567215.repository.UserRepository;
import com.example.spring_Yunhyeok_01023567215.repository.UserRoomRepository;
import com.example.spring_Yunhyeok_01023567215.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Initialization API", description = "초기 데이터 설정 관련 API")
public class InitializationController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserService userService;

    @Operation(summary = "초기 데이터 설정", description = "DB의 모든 데이터를 삭제한 후, 외부 API를 호출하여 사용자 데이터를 다시 채웁니다.")
    @PostMapping("/init")
    public ApiResponse<?> initializeData(
            @Parameter(description = "초기 데이터를 설정할 요청 객체") @RequestBody InitRequest initRequest) {

        // 1. 모든 테이블의 데이터 삭제
        userRoomRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();

        // 2. 외부 API 호출
        String apiUrl = String.format("https://fakerapi.it/api/v1/users?_seed=%s&_quantity=%d&_locale=ko_KR",
                initRequest.getSeed(), initRequest.getQuantity());
        RestTemplate restTemplate = new RestTemplate();
        UserResetApiResponse apiResponse = restTemplate.getForObject(apiUrl, UserResetApiResponse.class);

        // 3. API 응답에 따라 사용자 데이터 저장
        if (apiResponse != null && "OK".equals(apiResponse.getStatus())) {
            List<FakeUserData> users = apiResponse.getData();
            userService.resetUsers(users);
            return ApiResponse.success();
        } else {
            return ApiResponse.badRequest();
        }
    }
}
