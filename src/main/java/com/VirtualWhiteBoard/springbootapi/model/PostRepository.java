package com.VirtualWhiteBoard.springbootapi.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
