package com.milko.awsfilestorage.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class UserEntity {
    @Id
    private Integer id;
    private String username;
    private String password;
    private UserRole role;
    private String name;
    private Status status;

    @ToString.Include(name = "password")
    private String maskPassword(){
        return "********";
    }
}
