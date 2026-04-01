package com.java.vibecraft.dto.file;

public record FileNode(
        String path
) {

    @Override
    public String toString() {
        return path;
    }
}
