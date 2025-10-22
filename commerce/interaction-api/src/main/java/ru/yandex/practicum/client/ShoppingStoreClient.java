package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.shopping.ProductDto;
import ru.yandex.practicum.shopping.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @GetMapping("/api/v1/shopping-store")
    List<ProductDto> getByCategory(
            @RequestParam("category") ProductCategory category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String[] sort
    );

    @PutMapping("/api/v1/shopping-store")
    ProductDto create(@RequestBody ProductDto dto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto update(@RequestBody ProductDto dto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean remove(@RequestBody UUID productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean updateQuantity(@RequestBody SetProductQuantityStateRequest request);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto get(@PathVariable UUID productId);
}
