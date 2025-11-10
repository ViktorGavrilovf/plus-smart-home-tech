package ru.yandex.practicum.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.error.ApiError;
import ru.yandex.practicum.error.HttpStatusProvide;
import ru.yandex.practicum.error.exception.ProductNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handle(RuntimeException exception) {
        HttpStatusProvide statusProvide = (HttpStatusProvide) exception;

        ApiError error = ApiError.builder()
                .cause(exception.getCause())
                .stackTrace(exception.getStackTrace())
                .httpStatus(statusProvide.getStatus())
                .userMessage(exception.getMessage())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(statusProvide.getStatus()).body(error);
    }
}
