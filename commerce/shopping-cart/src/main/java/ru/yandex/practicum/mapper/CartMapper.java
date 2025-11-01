package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.model.CartItem;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public ShoppingCartDto toDto(ShoppingCart cart) {
        Map<UUID, Long> products = cart.getItems().stream()
                .collect(Collectors.toMap(CartItem::getProductId, CartItem::getQuantity));

        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(products)
                .build();
    }
}
