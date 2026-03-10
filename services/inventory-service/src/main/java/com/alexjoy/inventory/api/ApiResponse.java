package com.alexjoy.inventory.api;

public record ApiResponse<T>(
    String timestamp,
    int status,
    String message,
    String path,
    T data
) {
}
