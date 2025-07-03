package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Integer id;
    private String comment;
    private Integer rating;
    private LocalDateTime creationDate;
    private Integer gymId;
    private String userName;
    private boolean pointsAwarded;
}