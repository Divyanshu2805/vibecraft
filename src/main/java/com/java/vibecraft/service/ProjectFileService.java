package com.java.vibecraft.service;

import com.java.vibecraft.dto.file.FileContentResponse;
import com.java.vibecraft.dto.file.FileTreeResponse;

public interface ProjectFileService {
    FileTreeResponse getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);

    void saveFile(Long projectId, String filePath, String fileContent, Long userId);
}
