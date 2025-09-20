package dto.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceRemovedEvent extends HubEvent {
    @NotBlank
    String id;

    @Override
    HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
