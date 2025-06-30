package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewDTO;
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
    public ResponseEntity<List<ReviewDTO>> listByGym(@PathVariable Integer gymId) {
        return new ResponseEntity<>(reviewService.listByGym(gymId), HttpStatus.OK);
    }

    @PostMapping("/gym/{gymId}/user/{userId}")
    public ResponseEntity<ReviewDTO> create(@PathVariable Integer gymId,
                                            @PathVariable Long userId,
                                            @RequestBody ReviewCreateDTO reviewCreateDTO) {
        ReviewDTO newReview = reviewService.create(gymId, userId, reviewCreateDTO);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
}