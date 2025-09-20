package ru.practicum.mapper;

import ru.practicum.dto.hub.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.stream.Collectors;

@Component
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        switch (event.getType()) {
            case DEVICE_ADDED -> builder.setPayload(
                    DeviceAddedEventAvro.newBuilder()
                            .setId(((DeviceAddedEvent) event).getId())
                            .setType(DeviceTypeAvro.valueOf(((DeviceAddedEvent) event).getType().name()))
                            .build());

            case DEVICE_REMOVED -> builder.setPayload(
                    DeviceRemovedEventAvro.newBuilder()
                            .setId(((DeviceRemovedEvent) event).getId())
                            .build());

            case SCENARIO_ADDED -> {
                ScenarioAddedEvent added = (ScenarioAddedEvent) event;
                builder.setPayload(
                        ScenarioAddedEventAvro.newBuilder()
                                .setName(added.getName())
                                .setConditions(added.getConditions().stream()
                                        .map(c -> ScenarioConditionAvro.newBuilder()
                                                .setSensorId(c.getSensorId())
                                                .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                                                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                                                .setValue(c.getValue() == null ? null : c.getValue())
                                                .build()).collect(Collectors.toList()))
                                .setActions(added.getActions().stream()
                                        .map(a -> DeviceActionAvro.newBuilder()
                                                .setSensorId(a.getSensorId())
                                                .setType(ActionTypeAvro.valueOf(a.getType().name()))
                                                .setValue(a.getValue())
                                                .build()).collect(Collectors.toList()))
                                .build());
            }

            case SCENARIO_REMOVED -> builder.setPayload(
                    ScenarioRemovedEventAvro.newBuilder()
                            .setName(((ScenarioRemovedEvent) event).getName())
                            .build());
        }

        return builder.build();
    }
}
