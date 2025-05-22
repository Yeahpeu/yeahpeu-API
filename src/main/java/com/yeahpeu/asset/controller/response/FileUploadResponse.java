package com.yeahpeu.asset.controller.response;

import com.yeahpeu.asset.service.FileUploadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileUploadResponse {
    private String url;
    private String contentType;

    public static FileUploadResponse from(FileUploadDTO dto) {
        return new FileUploadResponse(
                dto.getUrl(),
                dto.getContentType()
        );
    }
}
