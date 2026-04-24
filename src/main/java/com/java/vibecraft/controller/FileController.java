package com.java.vibecraft.controller;

import com.java.vibecraft.dto.project.FileContentResponse;
import com.java.vibecraft.dto.project.FileNode;
import com.java.vibecraft.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {

    private final FileService projectFileService;

    @GetMapping
    public ResponseEntity<FileNode> getFileTree(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));
    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @RequestParam String path) {
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));
    }

}
