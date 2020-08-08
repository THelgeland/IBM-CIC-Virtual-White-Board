package com.VirtualWhiteBoard.springbootapi.controller;

import com.VirtualWhiteBoard.springbootapi.model.Post;
import com.VirtualWhiteBoard.springbootapi.model.PostRepository;
import com.VirtualWhiteBoard.springbootapi.util.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Virtual White Board Posts
 */
//TODO: Add links to responses and give option for XML
@RestController
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    List<Post> getAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    Post getOne(@PathVariable Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find " + id));
    }

    /**
     * Method for creating a new post. The owner is always set to be whoever is authenticated, and the creation date
     * is set as well
     * @param post The Post object to be inserted into the database
     * @return The inserted Post with the generated Id and the correct owner as well as creation date
     */
    @PostMapping("/")
    Post createPost(@RequestBody Post post) {
        post.setOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        post.setDateCreated(LocalDateTime.now());
        return postRepository.save(post);
    }

    /**
     * Method for deleting a given post. A post can only be deleted by its owner or a moderator.
     * @param id The Id identifying the post to be attempted deleted.
     */
    @DeleteMapping("/{id}")
    void deletePost(@PathVariable Long id) {
        if (!isAuthorizedToDelete(id)) {
            return;
        }
        postRepository.deleteById(id);
    }

    private boolean isAuthorizedToDelete(Long id) {
        Post post = getOne(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isModerator = authentication.getAuthorities().
                contains(new SimpleGrantedAuthority(Constants.MODERATOR_ROLE));
        boolean isOwner = authentication.getName().equals(post.getOwner());
        return isOwner || isModerator;
    }
}
