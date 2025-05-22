package com.yeahpeu.asset.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum FileType {
    IMAGE("image/", List.of("jpg", "jpeg", "png", "gif", "bmp", "webp")),
    PDF("application/pdf", List.of("pdf")),
    WORD("application/msword", List.of("doc")),
    WORD_NEW("application/vnd.openxmlformats-officedocument.wordprocessingml.document", List.of("docx")),
    HWP("application/vnd.hancom.hwp", List.of("hwp")),
    TEXT("text/plain", List.of("txt")),
    EXCEL("application/vnd.ms-excel", List.of("xls")),
    EXCEL_NEW("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", List.of("xlsx")),
    CSV("text/csv", List.of("csv")),
    PPT("application/vnd.ms-powerpoint", List.of("ppt")),
    PPT_NEW("application/vnd.openxmlformats-officedocument.presentationml.presentation", List.of("pptx"));

    private final String mimeType;
    private final List<String> extensions;

    FileType(String mimeType, List<String> extensions) {
        this.mimeType = mimeType;
        this.extensions = extensions;
    }

    public String getMimeType() {
        return mimeType;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    /**
     * 확장자로부터 MIME 타입 찾기
     */
    public static Optional<FileType> fromExtension(String extension) {
        return Arrays.stream(values())
                .filter(type -> type.extensions.contains(extension.toLowerCase()))
                .findFirst();
    }

    /**
     * MIME 타입 검증
     */
    public static boolean isValidMimeType(String mimeType) {
        return Arrays.stream(values())
                .anyMatch(type -> mimeType.startsWith(type.getMimeType()));
    }
}

