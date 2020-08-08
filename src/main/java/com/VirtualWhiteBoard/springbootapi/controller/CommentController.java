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
@RequestMapping("/{postId}")
public class CommentController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/comments")
    List<Comment> getAll(@PathVariable Long postId) {
        if (!postRepository.existsById(postId)) {
            return null;
        }
        return commentRepository.findByPostId(postId);
    }

    @PostMapping("/comments")
    Comment createComment(@RequestBody Comment comment, @PathVariable Long postId) {
        comment.setOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setDateCreated(LocalDateTime.now());
        comment.setPostId(postId);
        return commentRepository.save(comment);
    }
}
