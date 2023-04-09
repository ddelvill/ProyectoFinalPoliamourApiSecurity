package com.example.services;

import java.util.List;

import com.example.entities.Department;

public interface DepartmentService {
    public List <Department> findAll();
    public Department findbyId (long id); 
    public Department save (Department department);
    public void delete (Department department); 
}
