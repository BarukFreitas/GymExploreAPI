package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import java.util.Set;
import br.com.dbc.vemser.GymExploreAPI.entity.Role;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
    private Integer points;
}