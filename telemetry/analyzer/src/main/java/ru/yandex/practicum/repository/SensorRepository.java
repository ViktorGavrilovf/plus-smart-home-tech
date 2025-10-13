package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {

    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    @Transactional
    @Query("DELETE FROM ScenarioCondition sc WHERE sc.sensor.id = :sensorId")
    void deleteConditionsBySensorId(String sensorId);

    @Transactional
    @Query("DELETE FROM ScenarioAction sa WHERE sa.sensor.id = :sensorId")
    void deleteActionsBySensorId(String sensorId);
}
