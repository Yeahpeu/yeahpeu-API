package com.yeahpeu.ai.controller.requset;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class PromptRequest {

    private String nativePrompt;
    private List<PromptVariable> promptVariables;

    @Data
    public static class PromptVariable {

        private String key;
        private List<String> values;
        private int priority;
    }
}
