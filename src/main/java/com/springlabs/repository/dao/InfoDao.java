package com.springlabs.repository.dao;

import com.springlabs.model.Info;
import java.util.List;
import java.util.Optional;


public interface InfoDao {

    List<Info> findAll();
    Optional<Info> findById(Integer id);
    Info save(Info info);
    void delete(Integer id);
}