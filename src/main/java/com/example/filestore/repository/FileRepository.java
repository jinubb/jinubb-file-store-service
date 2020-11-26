package com.example.filestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filestore.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{

}
