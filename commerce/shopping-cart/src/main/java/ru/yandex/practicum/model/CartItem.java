package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "cart_items", schema = "shopping_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartItem {

    @Id
    @Column(name = "id", nullable = false)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    ShoppingCart cart;

    @Column(name = "product_id")
    UUID productId;

    @Column(name = "quantity", nullable = false)
    Long quantity;
}
