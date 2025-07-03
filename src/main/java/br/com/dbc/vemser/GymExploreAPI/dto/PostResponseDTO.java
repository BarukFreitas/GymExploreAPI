package br.com.dbc.vemser.GymExploreAPI.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long userId;
    private String username;
    private String imageUrl;
    private boolean pointsAwarded;
}