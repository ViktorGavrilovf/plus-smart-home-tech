package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
