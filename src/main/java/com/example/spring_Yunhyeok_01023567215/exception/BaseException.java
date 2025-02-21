package com.example.spring_Yunhyeok_01023567215.exception;

import java.util.HashMap;
import java.util.Map;

public class BaseException extends RuntimeException{
    private final Map<String, Object> details = new HashMap<>();

    public BaseException(String message) {
        super(message);

    }

    public BaseException(ExceptionMessage message) {
        super(message.getMessage());

    }

    public BaseException(ExceptionMessage message, Map<String, Object> details) {
        super(message.getMessage());
        if (details != null) {
            this.details.putAll(details);
        }
    }

    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }
}
