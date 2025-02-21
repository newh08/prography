package com.example.spring_Yunhyeok_01023567215.exception;

import com.example.spring_Yunhyeok_01023567215.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ApiResponse<String> handleBadRequest(BaseException ex) {
        return ApiResponse.badRequest();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleServerError(Exception ex) {
        return ApiResponse.serverError();
    }
}