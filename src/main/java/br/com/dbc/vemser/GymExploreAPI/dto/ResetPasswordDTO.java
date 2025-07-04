package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDTO {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}