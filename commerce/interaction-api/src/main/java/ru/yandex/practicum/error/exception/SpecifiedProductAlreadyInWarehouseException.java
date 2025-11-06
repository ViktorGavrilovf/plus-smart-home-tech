package ru.yandex.practicum.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException implements HttpStatusProvide {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public SpecifiedProductAlreadyInWarehouseException() {
        super("Товар с указанным идентификатором уже зарегистрирован на складе");
    }
}
