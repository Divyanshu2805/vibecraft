package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.file.FileContentResponse;
import com.java.vibecraft.dto.file.FileTreeResponse;
import com.java.vibecraft.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public FileTreeResponse getFileTree(Long projectId, Long userId) {
        return null;
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent, Long userId) {

    }
}
