package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ContactDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O formato do e-mail é inválido")
    private String email;

    @NotBlank(message = "O assunto não pode estar em branco")
    private String subject;

    @NotBlank(message = "A mensagem não pode estar em branco")
    private String message;
}