package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.shopping.ProductDto;
import ru.yandex.practicum.shopping.SetProductQuantityStateRequest;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreClient {

    @GetMapping
    Page<ProductDto> getByCategory(@RequestParam("category") ProductCategory category, Pageable pageable);

    @PostMapping
    ProductDto create(@RequestBody ProductDto dto);

    @PutMapping
    ProductDto update(@RequestBody ProductDto dto);

    @PostMapping("/removeProductFromStore")
    boolean remove(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean updateQuantity(SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto get(@PathVariable UUID productId);
}
