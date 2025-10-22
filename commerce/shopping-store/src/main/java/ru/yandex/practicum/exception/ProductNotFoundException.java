package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;
    public ProductNotFoundException(UUID id) {
        super("Продукт не найден" + id);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
