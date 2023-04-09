package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.YardDao;
import com.example.entities.Yard;

@Service
public class YardServiceImpl implements YardService {

    @Autowired
    private YardDao yardDao;

    @Override
    public List<Yard> findAll() {
        return yardDao.findAll();
    }

    @Override
    public Yard findbyId(long id) {
        return yardDao.findById(id).get();
    }

    @Override
    public Yard save(Yard yard) {
        return yardDao.save(yard);
    }

    @Override
    public void delete(Yard yard) {
        yardDao.delete(yard);
    }

    

    
}
