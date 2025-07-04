package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<ReviewResponseDTO>> listByGym(@PathVariable Integer gymId) {
        List<ReviewResponseDTO> reviews = reviewService.listByGym(gymId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/gym/{gymId}/user/{userId}")
    public ResponseEntity<ReviewResponseDTO> create(@PathVariable Integer gymId,
                                                    @PathVariable Long userId,
                                                    @RequestBody ReviewCreateDTO reviewCreateDTO) {
        ReviewResponseDTO newReview = reviewService.create(gymId, userId, reviewCreateDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) throws RegraDeNegocioException {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}