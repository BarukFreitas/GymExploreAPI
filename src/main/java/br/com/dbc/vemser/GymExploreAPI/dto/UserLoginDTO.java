package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;

// import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {
    // @NotBlank(message = "Nome de usuário é obrigatório")
    private String username;

    // @NotBlank(message = "Senha é obrigatória")
    private String password;
}