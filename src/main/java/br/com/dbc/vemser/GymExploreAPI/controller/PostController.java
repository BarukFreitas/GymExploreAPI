package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.PostCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.PostResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.PostEntity;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    private PostResponseDTO toResponseDTO(PostEntity postEntity) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(postEntity.getId());
        dto.setContent(postEntity.getContent());
        dto.setTimestamp(postEntity.getTimestamp());
        dto.setUserId(postEntity.getUser().getId());
        dto.setUsername(postEntity.getUser().getUsername());
        return dto;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<PostResponseDTO> createPost(
            @PathVariable Long userId,
            @RequestBody @Valid PostCreateDTO postCreateDTO) throws RegraDeNegocioException {
        PostEntity newPost = postService.createPost(userId, postCreateDTO.getContent());
        return new ResponseEntity<>(toResponseDTO(newPost), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        List<PostResponseDTO> response = posts.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(
            @PathVariable Long userId) throws RegraDeNegocioException {
        List<PostEntity> posts = postService.getPostsByUserId(userId);
        List<PostResponseDTO> response = posts.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId) throws RegraDeNegocioException {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}