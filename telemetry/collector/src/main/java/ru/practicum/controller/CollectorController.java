package ru.practicum.controller;

import ru.practicum.dto.hub.HubEvent;
import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.mapper.HubEventMapper;
import ru.practicum.mapper.SensorEventMapper;
import ru.practicum.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;
    private final ProducerService service;

    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensor(@Valid @RequestBody SensorEvent event) {
        service.sendSensorEvent(sensorEventMapper.toAvro(event));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHub(@Valid @RequestBody HubEvent event) {
        service.sendHubEvent(hubEventMapper.toAvro(event));
        return ResponseEntity.ok().build();
    }
}
