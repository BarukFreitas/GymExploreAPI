package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ReviewCreateDTO {

    @NotBlank
    private String comment;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}