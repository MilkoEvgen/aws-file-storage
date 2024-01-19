package com.milko.awsfilestorage.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {
    private Integer id;
    private String name;
}
