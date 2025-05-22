package com.yeahpeu.task.service;

import static java.util.stream.Collectors.toList;

import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.event.domain.EventEntity;
import com.yeahpeu.event.repository.EventRepository;
import com.yeahpeu.task.domain.TaskEntity;
import com.yeahpeu.task.repository.TaskRepository;
import com.yeahpeu.task.service.command.TaskCreateCommand;
import com.yeahpeu.task.service.command.TaskUpdateCommand;
import com.yeahpeu.task.service.command.UpdateStatusCommand;
import com.yeahpeu.task.service.dto.TaskDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<TaskDto> getTasksByEventId(Long eventId) {
        EventEntity event = findEventEntity(eventId);

        List<TaskEntity> tasks = taskRepository.findByEvent_Id(event.getId());

        return tasks.stream()
                .map(TaskDto::from)
                .collect(toList());
    }

    @Transactional
    @Override
    public TaskDto save(TaskCreateCommand command) {
        EventEntity event = findEventEntity(command.getEventId());

        TaskEntity task = TaskEntity.of(event, command.getName());

        return TaskDto.from(taskRepository.save(task));
    }

    @Transactional
    @Override
    public TaskDto updateTask(TaskUpdateCommand command) {
        TaskEntity task = findTaskEntityByEventIdAndTaskId(command.getTaskCreateCommand().getEventId(),
                command.getTaskId());

        task.updateDetails(command.getTaskCreateCommand());

        return TaskDto.from(task);
    }

    @Override
    public void deleteTask(Long eventId, Long taskId) {
        TaskEntity task = findTaskEntityByEventIdAndTaskId(eventId, taskId);

        taskRepository.delete(task);
    }

    @Transactional
    @Override
    public TaskDto updateTaskStatus(UpdateStatusCommand command) {
        TaskEntity task = findTaskEntityByEventIdAndTaskId(command.getEventId(), command.getTaskId());

        task.updateStatus(command.isCompleted());

        return TaskDto.from(task);
    }

    private EventEntity findEventEntity(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_EVENT_ID));
    }

    private TaskEntity findTaskEntityByEventIdAndTaskId(Long eventId, Long taskId) {
        return taskRepository.findByEvent_IdAndId(eventId, taskId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_TASK_ID));
    }
}
