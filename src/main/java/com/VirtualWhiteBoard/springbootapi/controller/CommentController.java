package com.VirtualWhiteBoard.springbootapi.controller;

import com.VirtualWhiteBoard.springbootapi.model.Comment;
import com.VirtualWhiteBoard.springbootapi.model.CommentRepository;
import com.VirtualWhiteBoard.springbootapi.model.Post;
import com.VirtualWhiteBoard.springbootapi.model.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}")
public class CommentController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Method for getting all comments associated with a given Post
     * @param postId The Post to get comments for
     * @return All comments associated with @postId
     */
    @GetMapping("/comments")
    List<Comment> getAll(@PathVariable Long postId) {
        if (!postRepository.existsById(postId)) {
            return null;
        }
        return commentRepository.findByPostId(postId);
    }

    /**
     * Method for adding a comment, the owner, date and correct postId are set before it is persisted
     * @param comment The comment to add
     * @param postId The post that the comment should be added to
     * @return The final comment built by the server
     */
    @PostMapping("/comments")
    Comment createComment(@RequestBody Comment comment, @PathVariable Long postId) {
        comment.setOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setDateCreated(LocalDateTime.now());
        comment.setPostId(postId);
        return commentRepository.save(comment);
    }
}
