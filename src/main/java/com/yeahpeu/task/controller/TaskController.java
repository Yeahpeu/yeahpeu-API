package com.yeahpeu.task.controller;

import com.yeahpeu.common.request.UpdateStatusRequest;
import com.yeahpeu.task.controller.request.TaskRequest;
import com.yeahpeu.task.service.TaskService;
import com.yeahpeu.task.service.command.TaskCreateCommand;
import com.yeahpeu.task.service.command.TaskUpdateCommand;
import com.yeahpeu.task.service.command.UpdateStatusCommand;
import com.yeahpeu.task.service.dto.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Task Controller",
        description = "확인 목록 관리"
)
@RequestMapping("/api/v1/wedding/events/{eventId}/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "확인 목록 조회",
            description = "등록된 이벤트 ID를 이용하여 확인 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable Long eventId) {
        List<TaskDto> taskDtos = taskService.getTasksByEventId(eventId);

        return ResponseEntity.ok(taskDtos);
    }

    @Operation(
            summary = "확인 목록 추가",
            description = "등록된 이벤트에 확인 목록을 추가합니다."
    )
    @PostMapping
    public ResponseEntity<TaskDto> addTask(@PathVariable Long eventId, @RequestBody TaskRequest taskRequest) {
        TaskDto saved = taskService.save(TaskCreateCommand.from(eventId, taskRequest));

        return ResponseEntity.ok(saved);
    }

    @Operation(
            summary = "확인 목록 수정",
            description = "등록된 확인 목록을 수정합니다."
    )
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long eventId,
                                              @PathVariable Long taskId,
                                              @RequestBody TaskRequest taskRequest) {
        TaskDto updated = taskService.updateTask(TaskUpdateCommand.from(eventId, taskId, taskRequest));

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "확인 목록 삭제",
            description = "등록된 확인 목록을 삭제합니다."
    )
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long eventId, @PathVariable Long taskId) {
        taskService.deleteTask(eventId, taskId);

        return ResponseEntity.noContent()
                .build();
    }

    @Operation(
            summary = "확인 목록 완료 상태 변화",
            description = "등록된 확인 목록의 완료 상태를 수정합니다."
    )
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long eventId,
                                                    @PathVariable Long taskId,
                                                    @RequestBody UpdateStatusRequest updateStatusRequest) {
        TaskDto taskDto = taskService.updateTaskStatus(UpdateStatusCommand.from(eventId, taskId, updateStatusRequest));

        return ResponseEntity.ok(taskDto);
    }
}
