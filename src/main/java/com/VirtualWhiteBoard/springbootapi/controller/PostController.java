package com.VirtualWhiteBoard.springbootapi.controller;

import com.VirtualWhiteBoard.springbootapi.model.Post;
import com.VirtualWhiteBoard.springbootapi.model.PostModelAssembler;
import com.VirtualWhiteBoard.springbootapi.model.PostRepository;
import com.VirtualWhiteBoard.springbootapi.util.Constants;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Virtual White Board Posts
 */
//TODO: Add links to responses and give option for XML
@RestController
public class PostController {

    private final PostRepository postRepository;
    private final PostModelAssembler modelAssembler;

    public PostController(PostRepository postRepository, PostModelAssembler modelAssembler) {
        this.postRepository = postRepository;
        this.modelAssembler = modelAssembler;
    }

    @GetMapping("/posts")
    public CollectionModel<EntityModel<Post>> getAll() {
        List<EntityModel<Post>> posts = postRepository.findAll().stream().map(modelAssembler::toModel).
                collect(Collectors.toList());
        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).getAll()).withSelfRel());
    }

    @GetMapping("/posts/{id}")
    public EntityModel<Post> getOne(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find " + id));
        return modelAssembler.toModel(post);
    }

    /**
     * Method for creating a new post. The owner is always set to be whoever is authenticated, and the creation date
     * is set as well
     * @param post The Post object to be inserted into the database
     * @return A ResponseEntity with the new post as the body, the 201 created code as well as a self-refering URI
     */
    @PostMapping("/posts")
    ResponseEntity<?> createPost(@RequestBody Post post) {
        post.setOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        post.setDateCreated(LocalDateTime.now());

        EntityModel<Post> newPost = modelAssembler.toModel(postRepository.save(post));
        return ResponseEntity.created(newPost.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(newPost);
    }

    /**
     * Method for deleting a given post. A post can only be deleted by its owner or a moderator.
     * @param id The Id identifying the post to be attempted deleted.
     */
    @DeleteMapping("/posts/{id}")
    ResponseEntity<?> deletePost(@PathVariable Long id) {
        if (!isAuthorizedToDelete(id)) {
            return new ResponseEntity<>("Unauthorized action", HttpStatus.UNAUTHORIZED);
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAuthorizedToDelete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find " + id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isModerator = authentication.getAuthorities().
                contains(new SimpleGrantedAuthority(Constants.MODERATOR_ROLE));
        boolean isOwner = authentication.getName().equals(post.getOwner());
        return isOwner || isModerator;
    }
}
