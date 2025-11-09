package ru.yandex.practicum.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

@Getter
public class NoDeliveryFoundException extends RuntimeException implements HttpStatusProvide {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public NoDeliveryFoundException() {
        super("Не найдена доставка для расчёта");
    }
}
