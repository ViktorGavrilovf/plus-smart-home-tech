package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "carts", schema = "shopping_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {

    @Id
    @Column(name = "shopping_cart_id", nullable = false, updatable = false)
    UUID shoppingCartId;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    CartState state;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    Set<CartItem> items = new HashSet<>();

    public enum CartState {
        ACTIVE,
        DEACTIVATED
    }
}
