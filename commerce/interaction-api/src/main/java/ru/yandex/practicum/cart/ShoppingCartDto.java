package ru.yandex.practicum.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ShoppingCartDto {

    @NotNull
    UUID shoppingCartId;

    @NotNull
    Map<UUID, Long> products;
}
