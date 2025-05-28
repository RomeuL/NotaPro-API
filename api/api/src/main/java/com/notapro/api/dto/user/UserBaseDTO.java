package com.notapro.api.dto.user;

import com.notapro.api.dto.BaseDTO;
import com.notapro.api.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserBaseDTO extends BaseDTO {
    private Integer id;
    private String nome;
    private String email;
    private Role role;
}