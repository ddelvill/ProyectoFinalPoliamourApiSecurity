package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Yard;

public interface YardDao extends JpaRepository<Yard, Long>   {
    
}
