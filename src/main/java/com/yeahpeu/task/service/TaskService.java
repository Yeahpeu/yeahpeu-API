package com.yeahpeu.task.service;

import com.yeahpeu.task.service.command.TaskCreateCommand;
import com.yeahpeu.task.service.command.TaskUpdateCommand;
import com.yeahpeu.task.service.command.UpdateStatusCommand;
import com.yeahpeu.task.service.dto.TaskDto;
import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksByEventId(Long eventId);

    TaskDto save(TaskCreateCommand command);

    TaskDto updateTask(TaskUpdateCommand command);

    void deleteTask(Long eventId, Long taskId);

    TaskDto updateTaskStatus(UpdateStatusCommand command);
}
