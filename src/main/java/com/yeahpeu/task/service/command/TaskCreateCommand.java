package com.yeahpeu.task.service.command;

import com.yeahpeu.task.controller.request.TaskRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCreateCommand {

    private Long eventId;

    private String name;

    public static TaskCreateCommand from(Long eventId, TaskRequest taskRequest) {
        return new TaskCreateCommand(eventId, taskRequest.getName());
    }
}
