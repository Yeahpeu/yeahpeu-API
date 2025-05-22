package com.yeahpeu.event.service.command;

import com.yeahpeu.common.request.UpdateStatusRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStatusCommand {

    private Long eventId;

    private boolean completed;

    public static UpdateStatusCommand from(Long eventId, UpdateStatusRequest updateStatusRequest) {
        return new UpdateStatusCommand(eventId, updateStatusRequest.isCompleted());
    }
}
