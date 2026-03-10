package com.alexjoy.order.api;

public record ApiResponse<T>(
    String timestamp,
    int status,
    String message,
    String path,
    T data
) {
}
