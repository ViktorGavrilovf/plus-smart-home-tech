package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "product_stock", schema = "warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStock {

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    UUID productId;

    @Column(name = "fragile", nullable = false)
    Boolean fragile;

    @Embedded
    DimensionEmbeddable dimension;

    @Column(name = "weight", nullable = false)
    Double weight;

    @Column(name = "quantity", nullable = false)
    Long quantity;
}
