package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries", schema = "delivery_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {

    @Id
    UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Column(name = "total_weight", nullable = false)
    Double totalWeight;

    @Column(name = "total_volume", nullable = false)
    Double totalVolume;

    @Column(name = "fragile", nullable = false)
    Boolean fragile;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false, length = 20)
    DeliveryState deliveryState;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "from_address_id")
    Address fromAddress;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "to_address_id")
    Address toAddress;
}
