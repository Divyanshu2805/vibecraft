package com.java.vibecraft.service;

import com.java.vibecraft.dto.project.FileContentResponse;
import com.java.vibecraft.dto.project.FileNode;

public interface FileService {
    FileNode getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);

    void saveFile(Long projectId, String filePath, String fileContent, Long userId);
}
