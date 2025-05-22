package com.yeahpeu.task.repository;

import com.yeahpeu.task.domain.TaskEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByEvent_Id(Long eventId);

    Optional<TaskEntity> findByEvent_IdAndId(Long eventId, Long id);
}
