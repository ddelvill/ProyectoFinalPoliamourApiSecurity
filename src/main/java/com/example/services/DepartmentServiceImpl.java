package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.DepartmentDao;
import com.example.entities.Department;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    @Override
    public Department findbyId(long id) {
        return departmentDao.findById(id).get();
    }

    @Override
    public Department save(Department department) {
        return departmentDao.save(department);
    }

    @Override
    public void delete(Department department) {
        departmentDao.delete(department);
    }
    
}
