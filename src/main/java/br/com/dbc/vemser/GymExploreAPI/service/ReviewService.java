package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import br.com.dbc.vemser.GymExploreAPI.entity.Review;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.repository.GymRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.ReviewRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;

    public ReviewDTO create(Integer gymId, Long userId, ReviewCreateDTO reviewCreateDTO) {

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada!"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));


        Review review = new Review();
        review.setComment(reviewCreateDTO.getComment());
        review.setRating(reviewCreateDTO.getRating());
        review.setGym(gym);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);


        return mapToDTO(savedReview);
    }

    public List<ReviewDTO> listByGym(Integer gymId) {
        return reviewRepository.findByGymId(gymId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreationDate(review.getCreationDate());
        dto.setGymId(review.getGym().getId());
        dto.setUserName(review.getUser().getUsername());
        return dto;
    }
}