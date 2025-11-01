package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ProductStock;

import java.util.UUID;

public interface ProductStockRepository extends JpaRepository<ProductStock, UUID> {
}
