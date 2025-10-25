package ru.yandex.practicum.error;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    Throwable cause;
    StackTraceElement[] stackTrace;
    HttpStatus httpStatus;
    String userMessage;
    String message;
    Throwable[] suppressed;
    String localizedMessage;
}
