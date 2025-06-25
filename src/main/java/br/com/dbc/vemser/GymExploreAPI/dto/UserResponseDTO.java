package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    // Não inclua a senha ou qualquer outra informação sensível aqui
}