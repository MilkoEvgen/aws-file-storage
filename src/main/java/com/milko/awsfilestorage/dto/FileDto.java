package com.milko.awsfilestorage.dto;

import com.milko.awsfilestorage.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileDto {
    private Integer id;
    private String location;
    private Status status;
}