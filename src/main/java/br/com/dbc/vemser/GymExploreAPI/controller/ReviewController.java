package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
// 1. Importe o novo DTO de resposta
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor // Usar injeção pelo construtor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/gym/{gymId}")
    // 2. Altere o tipo de retorno para usar o novo DTO
    public ResponseEntity<List<ReviewResponseDTO>> listByGym(@PathVariable Integer gymId) {
        // A chamada ao serviço já retorna a lista de DTOs pronta
        List<ReviewResponseDTO> reviews = reviewService.listByGym(gymId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/gym/{gymId}/user/{userId}")
    // 3. Altere o tipo de retorno para usar o novo DTO
    public ResponseEntity<ReviewResponseDTO> create(@PathVariable Integer gymId,
                                                    @PathVariable Long userId,
                                                    @RequestBody ReviewCreateDTO reviewCreateDTO) {
        // A chamada ao serviço já retorna o DTO de resposta completo
        ReviewResponseDTO newReview = reviewService.create(gymId, userId, reviewCreateDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}