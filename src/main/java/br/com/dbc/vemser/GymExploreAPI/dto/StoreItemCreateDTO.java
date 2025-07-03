package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class StoreItemCreateDTO {

    @NotBlank(message = "O nome do item não pode estar em branco.")
    private String name;

    private String description;

    @NotNull(message = "O custo em pontos é obrigatório.")
    @Positive(message = "O custo em pontos deve ser um número positivo.")
    private Integer pointsCost;
}