package com.example.parcial2.dto.response;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        String status,
        int code,
        String message,
        T data,
        OffsetDateTime timestamp
) {
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>("success", code, message, data, OffsetDateTime.now());
    }
}
