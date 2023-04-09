package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Post;

public interface PostService {

    public List <Post> findAll(Sort sort);
    public Page <Post> findAll (Pageable pageable);
    public Post findbyId (long id); 
    public Post save (Post post);
    public void delete (Post post); 
    public List<Post> findByUserId(long id);
}
