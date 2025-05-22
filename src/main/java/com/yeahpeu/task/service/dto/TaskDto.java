package com.yeahpeu.task.service.dto;

import com.yeahpeu.task.domain.TaskEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDto {

    private Long id;

    private String name;

    private boolean completed;

    @Builder
    private TaskDto(Long id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }


    public static TaskDto from(TaskEntity taskEntity) {
        return TaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .completed(taskEntity.isCompleted())
                .build();
    }
}
