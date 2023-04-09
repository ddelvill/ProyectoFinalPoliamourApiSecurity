package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.PostDao;
import com.example.entities.Post;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Override
    public List<Post> findAll(Sort sort) {
        return postDao.findAll(sort);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postDao.findAll(pageable);
    }

    @Override
    public Post findbyId(long id) {
        return postDao.findById(id);
    }

    @Override
    @Transactional
    public Post save(Post post) {
        return postDao.save(post);
    }

    @Override
    @Transactional
    public void delete(Post post) {
        postDao.delete(post);
    }

    @Override
    public List<Post> findByUserId(long id) {
        return postDao.findByUserId(id);
    }

    


    
}
