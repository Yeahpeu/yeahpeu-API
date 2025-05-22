package com.yeahpeu.task.service.command;

import com.yeahpeu.task.controller.request.TaskRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskUpdateCommand {

    private Long taskId;

    private TaskCreateCommand taskCreateCommand;

    public static TaskUpdateCommand from(Long eventId, Long taskId, TaskRequest taskRequest) {
        return new TaskUpdateCommand(taskId, new TaskCreateCommand(eventId, taskRequest.getName()));
    }
}
