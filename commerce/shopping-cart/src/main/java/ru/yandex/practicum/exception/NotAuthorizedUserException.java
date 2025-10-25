package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

@Getter
public class NotAuthorizedUserException extends RuntimeException implements HttpStatusProvide {

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public NotAuthorizedUserException(String message) {
        super("Имя пользователя не должно быть пустым");
    }
}
