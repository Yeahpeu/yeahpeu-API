package com.yeahpeu.asset.controller;

import com.yeahpeu.asset.controller.response.FileUploadResponse;
import com.yeahpeu.asset.service.S3Service;
import com.yeahpeu.common.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/assets")
@RestController
public class AssetController {
    private final S3Service s3Service;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "이미지 파일 업로드", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart final MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("파일이 비어있습니다. 다시 업로드 해주세요");
        }
        return ResponseEntity.ok(FileUploadResponse.from(s3Service.upload(file)));
    }
}
