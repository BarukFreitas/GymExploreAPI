package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;

@Data
public class GymCreateDTO {
    private String name;
    private String address;
    private String phone;
    private String imageUrl;
}