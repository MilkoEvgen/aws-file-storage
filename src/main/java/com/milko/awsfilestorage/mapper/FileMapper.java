package com.milko.awsfilestorage.mapper;

import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.model.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileDto map(FileEntity fileEntity){
        if ( fileEntity == null ) {
            return null;
        }
        return FileDto.builder()
                .id(fileEntity.getId())
                .location(fileEntity.getLocation())
                .status(fileEntity.getStatus())
                .build();
    }

    public FileEntity map(FileDto fileDto){
        if ( fileDto == null ) {
            return null;
        }
        return FileEntity.builder()
                .id(fileDto.getId())
                .location(fileDto.getLocation())
                .status(fileDto.getStatus())
                .build();
    }
}
