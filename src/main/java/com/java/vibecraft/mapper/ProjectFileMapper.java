package com.java.vibecraft.mapper;

import com.java.vibecraft.dto.project.FileNode;
import com.java.vibecraft.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
