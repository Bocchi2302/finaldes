package com.example.parcial2.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        String status,
        int code,
        String message,
        List<String> errors,
        OffsetDateTime timestamp,
        String path
) {
    public static ApiErrorResponse of(int code, String message, List<String> errors, String path) {
        return new ApiErrorResponse("error", code, message, errors, OffsetDateTime.now(), path);
    }
}
