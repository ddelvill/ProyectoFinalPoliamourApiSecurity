package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.User;

public interface UserDao extends JpaRepository<User, Long> {

     //Recupera lista de usuarios ordenados

     //@Query(value = "select u from User u left join fetch u.department left join fetch u.hobbies left join fetch u.yards left join fetch u.phones")
    @Query(value = "select u from User u left join fetch u.department")
     public List<User> findAll(Sort sort);


    // //Recupera una pagina de usuarios
    // @Query(value = "select u from User u left join fetch u.department left join fetch u.hobbies left join fetch u.yards left join fetch u.phones",
    //  countQuery = "select count(u) from User u left join  u.department left join u.hobbies left join u.yards left join u.phones")
    @Query(value = "select u from User u left join fetch u.department",
    countQuery = "select count(u) from User u left join  u.department")
  

     public Page<User> findAll(Pageable pageable);


    //El metodo siguiente recupera el usuario por ID, para que nos traiga el resto de tablas

   // @Query(value = "select u from User u left join fetch u.department left join fetch u.hobbies left join fetch u.yards left join fetch u.phones where u.id= :id")
    @Query(value = "select u from User u left join fetch u.department where u.id= :id")
    public User findById(long id);
    


}

    

