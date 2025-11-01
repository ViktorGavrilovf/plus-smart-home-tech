package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

import java.util.UUID;

@Getter
public class ProductNotFoundException extends RuntimeException implements HttpStatusProvide {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ProductNotFoundException(UUID id) {
        super("Продукт не найден" + id);
    }
}
