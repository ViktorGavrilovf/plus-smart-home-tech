package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartClient {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProducts(@RequestParam("username") String username,
                                @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void deactivate(@RequestParam("username") String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@RequestParam("username") String username,
                                   @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam("username") String username,
                                   @RequestBody ChangeProductQuantityRequest request);
}
