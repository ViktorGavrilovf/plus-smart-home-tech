package ru.practicum.dto.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    String id;

    @NotNull
    DeviceType type;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    public enum DeviceType {
        MOTION_SENSOR,
        TEMPERATURE_SENSOR,
        LIGHT_SENSOR,
        CLIMATE_SENSOR,
        SWITCH_SENSOR
    }
}
