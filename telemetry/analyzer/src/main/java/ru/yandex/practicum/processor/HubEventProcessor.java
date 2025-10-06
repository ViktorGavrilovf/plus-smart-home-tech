package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${analyzer.topics.hub-events}")
    private String hubEventsTopic;

    @Override
    public void run() {
        log.info("Запуск HubEventProcessor. Подписка на топик: {}", hubEventsTopic);

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer-hub-events-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.deserializer.HubEventDeserializer");
        props.put("schema", HubEventAvro.getClassSchema());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        try (KafkaConsumer<String, HubEventAvro> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(hubEventsTopic));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(200));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    processEvent(record.value());
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка в HubEventProcessor: ", e);
        }
    }

    private void processEvent(HubEventAvro event) {
        log.debug("Получено событие HubEventAvro: {}", event);
        String hubId = event.getHubId();

        switch (event.getPayload().getClass().getSimpleName()) {
            case "DeviceAddedEventAvro" -> handleDeviceAdded(hubId, (DeviceAddedEventAvro) event.getPayload());
            case "DeviceRemovedEventAvro" -> handleDeviceRemoved((DeviceRemovedEventAvro) event.getPayload());
            case "ScenarioAddedEventAvro" -> handleScenarioAdded(hubId, (ScenarioAddedEventAvro) event.getPayload());
            case "ScenarioRemovedEventAvro" -> handleScenarioRemoved(hubId, (ScenarioRemovedEventAvro) event.getPayload());
            default -> log.warn("Неизвестный тип события: {}", event.getPayload().getClass());
        }
    }

    private void handleDeviceAdded(String hubId, DeviceAddedEventAvro payload) {
        if (sensorRepository.existsById(payload.getId())) {
            log.info("Сенсор {} уже зарегистрирован, пропускаем", payload.getId());
            return;
        }

        Sensor sensor = Sensor.builder()
                .id(payload.getId())
                .hubId(hubId)
                .build();
        sensorRepository.save(sensor);
        log.info("Добавлен новый сенсор: {} (hubId={})", payload.getId(), hubId);
    }

    private void handleDeviceRemoved(DeviceRemovedEventAvro payload) {
        String sensorId = payload.getId();
        sensorRepository.findById(sensorId).ifPresentOrElse(sensor -> {
            sensorRepository.delete(sensor);
            log.info("Удалён сенсор: {}", sensorId);
        }, () -> log.info("Попытка удалить несуществующий сенсор: {}", sensorId));
    }

    private void handleScenarioAdded(String hubId, ScenarioAddedEventAvro payload) {
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, payload.getName())
                .orElseGet(() -> Scenario.builder()
                        .hubId(hubId)
                        .name(payload.getName())
                        .build());

        for (ScenarioConditionAvro cond : payload.getConditions()) {
            Condition condition = Condition.builder()
                    .type(cond.getType().name())
                    .operation(cond.getOperation().name())
                    .value((Integer) cond.getValue())
                    .build();
            conditionRepository.save(condition);
        }

        for (DeviceActionAvro a : payload.getActions()) {
            Action action = Action.builder()
                    .type(a.getType().name())
                    .value(a.getValue())
                    .build();
            actionRepository.save(action);
        }

        scenarioRepository.save(scenario);
        log.info("Добавлен сценарий {} для хаба {}", scenario.getName(), hubId);
    }

    private void handleScenarioRemoved(String hubId, ScenarioRemovedEventAvro payload) {
        scenarioRepository.findByHubIdAndName(hubId, payload.getName()).ifPresentOrElse(scenario -> {
            scenarioRepository.delete(scenario);
            log.info("Удалён сценарий {} для хаба {}", payload.getName(), hubId);
        }, () -> log.info("Сценарий '{}' для хаба {} не найден", payload.getName(), hubId));
    }
}
