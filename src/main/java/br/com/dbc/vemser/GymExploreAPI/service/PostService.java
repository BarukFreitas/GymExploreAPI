package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.PostEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.PostRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe esta anotação

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional // Este já estava correto para a criação
    public PostEntity createPost(Long userId, String content, String imageUrl) throws RegraDeNegocioException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        PostEntity post = new PostEntity(content, imageUrl, user);

        return postRepository.save(post);
    }

    @Transactional // Adicione esta anotação aqui
    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByTimestampDesc();
    }

    @Transactional // Boa prática adicionar aqui também, se já não estiver
    public List<PostEntity> getPostsByUserId(Long userId) throws RegraDeNegocioException {
        if (!userRepository.existsById(userId)) {
            throw new RegraDeNegocioException("Usuário não encontrado para buscar postagens.");
        }
        return postRepository.findByUser_Id(userId);
    }

    @Transactional // Este já estava correto para a exclusão
    public void deletePost(Long postId) throws RegraDeNegocioException {
        if (!postRepository.existsById(postId)) {
            throw new RegraDeNegocioException("Postagem não encontrada para exclusão.");
        }
        postRepository.deleteById(postId);
    }
}