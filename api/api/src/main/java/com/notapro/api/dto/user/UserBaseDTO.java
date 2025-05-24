package com.notapro.api.dto.user;

import com.notapro.api.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserBaseDTO {
    private Integer id;
    private String nome;
    private String email;
    private Role role;
}