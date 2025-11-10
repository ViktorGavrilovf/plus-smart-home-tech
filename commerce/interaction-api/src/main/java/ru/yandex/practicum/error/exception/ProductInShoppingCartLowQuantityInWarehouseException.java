package ru.yandex.practicum.error.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.error.HttpStatusProvide;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class ProductInShoppingCartLowQuantityInWarehouseException extends RuntimeException implements HttpStatusProvide {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final Map<UUID, Long> missingProducts;

    public ProductInShoppingCartLowQuantityInWarehouseException(Map<UUID, Long> missingProducts) {
        super(buildMessage(missingProducts));
        this.missingProducts = missingProducts;
    }

    private static String buildMessage(Map<UUID, Long> missingProducts) {
        if (missingProducts == null || missingProducts.isEmpty()) {
            return "Товара/товаров в требуемом количестве нет на складе";
        }

        return missingProducts.entrySet().stream()
                .map(e -> String.format("Товара %s — не хватает %d шт.", e.getKey(), e.getValue()))
                .collect(Collectors.joining());
    }
}
