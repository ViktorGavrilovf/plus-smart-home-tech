package ru.yandex.practicum.error;

import org.springframework.http.HttpStatus;

public interface HttpStatusProvide {
    HttpStatus getStatus();
}
