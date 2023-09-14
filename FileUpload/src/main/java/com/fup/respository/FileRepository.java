package com.fup.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fup.entity.FileDB;

@Repository
public interface FileRepository extends JpaRepository<FileDB, String> {

}
