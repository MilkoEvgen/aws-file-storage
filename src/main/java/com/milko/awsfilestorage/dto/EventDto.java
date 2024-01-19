package com.milko.awsfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.milko.awsfilestorage.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventDto {
    private Integer id;
    @JsonIgnore
    private UserDto user;
    private FileDto file;
    private Status status;
}