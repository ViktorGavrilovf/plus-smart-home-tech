package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.NotFoundCartException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.CartItem;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final CartMapper mapper;
    private final WarehouseClient warehouseClient;

    @Transactional(readOnly = true)
    public ShoppingCartDto getCart(String username) {
        validateUser(username);
        ShoppingCart cart = repository.findByUsername(username)
                .orElseGet(() -> createNewCart(username));
        return mapper.toDto(cart);
    }

    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> products) {
        validateUser(username);
        ShoppingCart cart = repository.findByUsername(username)
                .orElseGet(() -> createNewCart(username));

        checkCartActive(cart);

        products.forEach((productId, quantity) -> cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        i -> i.setQuantity(i.getQuantity() + quantity),
                        () -> cart.getItems().add(CartItem.builder()
                                .id(UUID.randomUUID())
                                .cart(cart)
                                .productId(productId)
                                .quantity(quantity)
                                .build())
                ));

        repository.save(cart);
        ShoppingCartDto dto = mapper.toDto(cart);
        warehouseClient.checkAvailability(dto);

        return dto;
    }

    @Transactional
    public void deactivate(String username) {
        validateUser(username);
        ShoppingCart cart = repository.findByUsername(username)
                .orElseGet(() -> createNewCart(username));

        cart.setState(ShoppingCart.CartState.DEACTIVATED);
        repository.save(cart);
    }

    @Transactional
    public ShoppingCartDto removeProducts(String username, List<UUID> productsIds) {
        validateUser(username);
        ShoppingCart cart = repository.findByUsername(username)
                .orElseThrow(NotFoundCartException::new);

        checkCartActive(cart);

        if (!cart.getItems().removeIf(i -> productsIds.contains(i.getProductId()))) {
            throw new NoProductsInShoppingCartException();
        }

        repository.save(cart);
        return mapper.toDto(cart);
    }

    @Transactional
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        validateUser(username);
        ShoppingCart cart = repository.findByUsername(username)
                .orElseThrow(NotFoundCartException::new);

        checkCartActive(cart);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(NoProductsInShoppingCartException::new);

        item.setQuantity(request.getNewQuantity());
        repository.save(cart);
        ShoppingCartDto dto = mapper.toDto(cart);
        warehouseClient.checkAvailability(dto);

        return dto;
    }

    private void validateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
    }

    private void checkCartActive(ShoppingCart cart) {
        if (cart.getState().equals(ShoppingCart.CartState.DEACTIVATED)) {
            throw new IllegalArgumentException("Корзина деактивирована и недоступна для изменений");
        }
    }

    private ShoppingCart createNewCart(String username) {
        return repository.save(ShoppingCart.builder()
                .shoppingCartId(UUID.randomUUID())
                .username(username)
                .state(ShoppingCart.CartState.ACTIVE)
                .build());
    }
}
