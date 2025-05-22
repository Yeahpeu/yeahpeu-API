package com.yeahpeu.task.service.command;

import com.yeahpeu.common.request.UpdateStatusRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStatusCommand {

    private Long eventId;

    private Long taskId;

    private boolean completed;

    public static UpdateStatusCommand from(Long eventId, Long taskId, UpdateStatusRequest updateStatusRequest) {
        return new UpdateStatusCommand(eventId, taskId, updateStatusRequest.isCompleted());
    }
}
