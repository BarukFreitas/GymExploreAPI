package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.PostEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.PostRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PostEntity createPost(Long userId, String content) throws RegraDeNegocioException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        PostEntity post = new PostEntity(content, user);

        return postRepository.save(post);
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByTimestampDesc();
    }

    public List<PostEntity> getPostsByUserId(Long userId) throws RegraDeNegocioException {
        if (!userRepository.existsById(userId)) {
            throw new RegraDeNegocioException("Usuário não encontrado para buscar postagens.");
        }
        return postRepository.findByUser_Id(userId);
    }

    @Transactional
    public void deletePost(Long postId) throws RegraDeNegocioException {
        if (!postRepository.existsById(postId)) {
            throw new RegraDeNegocioException("Postagem não encontrada para exclusão.");
        }
        postRepository.deleteById(postId);
    }
}