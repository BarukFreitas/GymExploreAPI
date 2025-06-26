package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;


@Data
public class UserLoginDTO {
    private String username;

    private String password;
}