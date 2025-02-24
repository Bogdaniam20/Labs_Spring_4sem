package com.springlabs.repository.dao;

import com.springlabs.model.Info;
import com.springlabs.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InfoDaoImpl implements InfoDao {

    @Autowired
    private InfoRepository infoRepository;

    @Override
    public List<Info> findAll() {
        return infoRepository.findAll();
    }

    @Override
    public Optional<Info> findById(Integer id) {
        return infoRepository.findById(id);
    }

    @Override
    public Info save(Info info) {
        return infoRepository.save(info);
    }

    @Override
    public void delete(Integer id) {
        infoRepository.deleteById(id);
    }
}