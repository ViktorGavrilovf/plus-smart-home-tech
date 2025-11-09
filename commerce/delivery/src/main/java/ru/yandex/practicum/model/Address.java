package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "addresses", schema = "delivery_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    UUID addressId;
    String country;
    String city;
    String street;
    String house;
    String flat;
}
