package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import br.com.dbc.vemser.GymExploreAPI.entity.Review;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
import br.com.dbc.vemser.GymExploreAPI.repository.GymRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.ReviewRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;

    public ReviewResponseDTO create(Integer gymId, Long userId, ReviewCreateDTO reviewCreateDTO) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada!"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado!"));

        Review review = new Review();
        review.setComment(reviewCreateDTO.getComment());
        review.setRating(reviewCreateDTO.getRating());
        review.setGym(gym);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        boolean pointsWereAwarded = gamificationService.awardPoints(user, PointAction.CREATE_REVIEW);

        return toReviewResponseDTO(savedReview, pointsWereAwarded);
    }

    @Transactional
    public List<ReviewResponseDTO> listByGym(Integer gymId) {
        return reviewRepository.findByGymId(gymId).stream()
                .map(review -> toReviewResponseDTO(review, false))
                .collect(Collectors.toList());
    }

    private ReviewResponseDTO toReviewResponseDTO(Review review, boolean pointsAwarded) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreationDate(review.getCreationDate());
        dto.setGymId(review.getGym().getId());
        dto.setUserName(review.getUser().getUsername());
        dto.setPointsAwarded(pointsAwarded);
        return dto;
    }
}