package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;

// import javax.validation.constraints.Email;
// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.Size;

@Data
public class UserRegisterDTO {
    // @NotBlank(message = "Nome de usuário é obrigatório")
    // @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
    private String username;

    // @NotBlank(message = "Senha é obrigatória")
    // @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    // @NotBlank(message = "E-mail é obrigatório")
    // @Email(message = "E-mail inválido")
    private String email;
}