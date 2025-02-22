package com.springlabs.repository;

import com.springlabs.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info,Integer> {}
