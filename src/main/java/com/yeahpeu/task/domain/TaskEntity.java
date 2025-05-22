package com.yeahpeu.task.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.task.service.command.TaskCreateCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
public class TaskEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean completed = false;

    public static TaskEntity of(EventEntity event, String name) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setEvent(event);
        taskEntity.name = name;

        return taskEntity;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
        event.getTaskList().add(this);
    }

    public void updateDetails(TaskCreateCommand command) {
        name = command.getName();
    }

    public void updateStatus(boolean completed) {
        this.completed = completed;
    }
}
