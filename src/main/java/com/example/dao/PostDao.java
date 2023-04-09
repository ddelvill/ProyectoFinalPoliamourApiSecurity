package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Post;

public interface PostDao extends JpaRepository<Post, Long> {


    @Query(value = "select p from Post p left join fetch p.user")
     public List<Post> findAll(Sort sort);

    @Query(value = "select p from Post p left join fetch p.user",
    countQuery = "select count(p) from Post p left join  p.user")
  

     public Page<Post> findAll(Pageable pageable);

    @Query(value = "select p from Post p left join fetch p.user where p.id= :id")
    public Post findById(long id);

    @Query(value = "select p from Post p join fetch p.user u where u.id= :id")
    public List<Post> findByUserId(long id);
    
}
