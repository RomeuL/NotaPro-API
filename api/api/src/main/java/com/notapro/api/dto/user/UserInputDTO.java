package com.notapro.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserInputDTO extends UserBaseDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Override
    public String getNome() {
        return super.getNome();
    }
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Override
    public String getEmail() {
        return super.getEmail();
    }
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    
    @NotNull(message = "Role é obrigatório")
    @Override
    public com.notapro.api.model.enums.Role getRole() {
        return super.getRole();
    }
}