package com.example.services;

import java.util.List;

import com.example.entities.Yard;

public interface YardService {
    
    public List <Yard> findAll();
    public Yard findbyId (long id); 
    public Yard save (Yard yard);
    public void delete (Yard yard); 
}
