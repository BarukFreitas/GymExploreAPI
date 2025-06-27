package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PostCreateDTO {

    @NotBlank(message = "O conteúdo da postagem não pode estar vazio.")
    @Size(max = 1000, message = "A postagem não pode ter mais de 1000 caracteres.")
    private String content;
}