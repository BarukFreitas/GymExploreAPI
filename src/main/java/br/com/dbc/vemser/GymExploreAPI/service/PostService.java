package br.com.dbc.vemser.GymExploreAPI.service;

// 1. Importe os DTOs que vamos usar
import br.com.dbc.vemser.GymExploreAPI.dto.PostCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.PostResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.PostEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.PostRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;

    @Transactional
    // 2. Altere o método para receber um DTO de criação e retornar um DTO de resposta
    public PostResponseDTO createPost(Long userId, PostCreateDTO postCreateDTO) throws RegraDeNegocioException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RegraDeNegocioException("Utilizador não encontrado."));

        // Cria a entidade a partir do DTO
        PostEntity post = new PostEntity(postCreateDTO.getContent(), postCreateDTO.getImageUrl(), user);
        PostEntity savedPost = postRepository.save(post);

        // 3. Captura o resultado booleano do serviço de gamificação
        boolean pointsWereAwarded = gamificationService.awardPoints(user, PointAction.CREATE_POST);

        // 4. Retorna o DTO de resposta, incluindo a informação sobre os pontos
        return toPostResponseDTO(savedPost, pointsWereAwarded);
    }

    // O seu método getAllPosts também deve retornar uma lista de DTOs
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAllByOrderByTimestampDesc().stream()
                .map(post -> toPostResponseDTO(post, false)) // Assumimos false, pois esta info só é relevante na criação
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> getPostsByUserId(Long userId) throws RegraDeNegocioException {
        if (!userRepository.existsById(userId)) {
            throw new RegraDeNegocioException("Utilizador não encontrado para buscar postagens.");
        }
        return postRepository.findByUser_Id(userId).stream()
                .map(post -> toPostResponseDTO(post, false))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId) throws RegraDeNegocioException {
        if (!postRepository.existsById(postId)) {
            throw new RegraDeNegocioException("Postagem não encontrada para exclusão.");
        }
        postRepository.deleteById(postId);
    }

    private PostResponseDTO toPostResponseDTO(PostEntity post, boolean pointsAwarded) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setTimestamp(post.getTimestamp());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setPointsAwarded(pointsAwarded);
        return dto;
    }
}