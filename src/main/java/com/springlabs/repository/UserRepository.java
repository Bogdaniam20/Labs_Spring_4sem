package com.springlabs.repository;

import com.springlabs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.name = ?1 AND u.surname = ?2")
    List<User> findByNameAndSurname(String name, String surname);

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    List<User> findByName(String name);

    @Query("SELECT u FROM User u WHERE u.surname = ?1")
    List<User> findBySurname(String surname);
}