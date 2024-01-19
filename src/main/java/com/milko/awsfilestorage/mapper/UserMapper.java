package com.milko.awsfilestorage.mapper;

import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto map(UserEntity userEntity){
        if ( userEntity == null ) {
            return null;
        }
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .name(userEntity.getName())
                .status(userEntity.getStatus())
                .build();
    }

    public UserEntity map(UserDto userDto){
        if ( userDto == null ) {
            return null;
        }
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .name(userDto.getName())
                .status(userDto.getStatus())
                .build();
    }
}
