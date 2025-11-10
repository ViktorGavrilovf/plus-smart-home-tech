package ru.yandex.practicum.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

import java.util.UUID;

@Getter
public class PaymentNotFoundException extends RuntimeException implements HttpStatusProvide {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public PaymentNotFoundException() {
        super("Платёж не найден");
    }
}

