package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.shopping.ProductDto;
import ru.yandex.practicum.shopping.SetProductQuantityStateRequest;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ProductService service;

    @GetMapping
    @Override
    public List<ProductDto> getByCategory(@RequestParam("category") ProductCategory category,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) String[] sort) {
        Pageable pageable = PageRequest.of(page, size);
        return service.findByCategory(category, pageable);
    }

    @Override
    @PutMapping
    public ProductDto create(@Valid @RequestBody ProductDto dto) {
        return service.create(dto);
    }

    @Override
    @PostMapping
    public ProductDto update(@Valid @RequestBody ProductDto dto) {
        return service.update(dto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public boolean remove(@RequestBody UUID productId) {
        return service.removeFromStore(productId);
    }

    @Override
    @PostMapping("/quantityState")
    public boolean updateQuantity(@Valid @RequestBody SetProductQuantityStateRequest request) {
        return service.setQuantityState(request);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto get(@PathVariable UUID productId) {
        return service.getById(productId);
    }
}
