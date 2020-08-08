package com.VirtualWhiteBoard.springbootapi.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.VirtualWhiteBoard.springbootapi.controller.PostController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {
    @Override
    public EntityModel<Post> toModel(Post post) {
        return EntityModel.of(post, linkTo(methodOn(PostController.class).getOne(post.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).getAll()).withRel("posts"));
    }
}
