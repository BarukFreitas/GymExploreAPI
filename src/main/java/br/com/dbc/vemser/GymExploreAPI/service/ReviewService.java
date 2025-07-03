package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.ReviewCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.ReviewResponseDTO; // 1. Importar o novo DTO de resposta
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import br.com.dbc.vemser.GymExploreAPI.entity.Review;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
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
    private final GamificationService gamificationService;

    public ReviewResponseDTO create(Integer gymId, Long userId, ReviewCreateDTO reviewCreateDTO) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada!"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado!"));

        Review review = new Review();
        review.setComment(reviewCreateDTO.getComment()); // Adicionado para definir o comentário
        review.setRating(reviewCreateDTO.getRating());   // Adicionado para definir a nota
        review.setGym(gym);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        // 3. Captura o resultado booleano do serviço de gamificação
        boolean pointsWereAwarded = gamificationService.awardPoints(user, PointAction.CREATE_REVIEW);

        // 4. Retorna o DTO de resposta, incluindo a informação sobre os pontos
        return toReviewResponseDTO(savedReview, pointsWereAwarded);
    }

    public List<ReviewResponseDTO> listByGym(Integer gymId) {
        return reviewRepository.findByGymId(gymId).stream()
                .map(review -> toReviewResponseDTO(review, false)) // Assumimos false, pois esta info só é relevante na criação
                .collect(Collectors.toList());
    }

    // 5. Crie um método privado para converter a entidade para o DTO de resposta
    private ReviewResponseDTO toReviewResponseDTO(Review review, boolean pointsAwarded) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreationDate(review.getCreationDate());
        dto.setGymId(review.getGym().getId());
        dto.setUserName(review.getUser().getUsername());
        dto.setPointsAwarded(pointsAwarded); // Define se os pontos foram atribuídos
        return dto;
    }
}