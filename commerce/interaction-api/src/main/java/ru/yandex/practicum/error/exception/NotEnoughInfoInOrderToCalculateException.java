package ru.yandex.practicum.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

@Getter
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException implements HttpStatusProvide {

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public NotEnoughInfoInOrderToCalculateException() {
        super("Недостаточно информации в заказе для расчёта");
    }
}
