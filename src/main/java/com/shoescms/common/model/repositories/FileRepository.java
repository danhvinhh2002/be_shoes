package com.shoescms.common.model.repositories;

import com.shoescms.common.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("select p from FileEntity p where p.id = ?1")
    Optional<FileEntity> findImageById(Long id);
}
