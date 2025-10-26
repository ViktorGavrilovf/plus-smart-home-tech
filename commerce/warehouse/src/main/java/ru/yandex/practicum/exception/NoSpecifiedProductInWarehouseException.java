package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

@Getter
public class NoSpecifiedProductInWarehouseException extends RuntimeException implements HttpStatusProvide {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public NoSpecifiedProductInWarehouseException() {
        super("Нет информации о товаре на складе");
    }
}
