package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.project.FileContentResponse;
import com.java.vibecraft.dto.project.FileNode;
import com.java.vibecraft.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public FileNode getFileTree(Long projectId) {
        return null;
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {

    }
}
