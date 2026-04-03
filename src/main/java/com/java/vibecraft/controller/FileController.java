package com.java.vibecraft.controller;

import com.java.vibecraft.dto.file.FileContentResponse;
import com.java.vibecraft.dto.file.FileTreeResponse;
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
    public ResponseEntity<FileTreeResponse> getFileTree(@PathVariable Long projectId) {
        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileTree(projectId, userId));
    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @RequestParam String path) {
        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path, userId));
    }

}
