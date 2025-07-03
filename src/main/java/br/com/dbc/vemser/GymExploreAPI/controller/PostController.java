package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.PostCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.PostResponseDTO;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.service.PostService;
import lombok.RequiredArgsConstructor; // Importar
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/{userId}")
    public ResponseEntity<PostResponseDTO> createPost(
            @PathVariable Long userId,
            @RequestBody @Valid PostCreateDTO postCreateDTO) throws RegraDeNegocioException {

        PostResponseDTO newPostResponse = postService.createPost(userId, postCreateDTO);

        return new ResponseEntity<>(newPostResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> response = postService.getAllPosts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(
            @PathVariable Long userId) throws RegraDeNegocioException {
        List<PostResponseDTO> response = postService.getPostsByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId) throws RegraDeNegocioException {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}