package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ForgotPasswordDTO {
    @NotBlank
    @Email
    private String email;
}