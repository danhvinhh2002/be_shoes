package com.shoescms.domain.product.repository;

import com.shoescms.domain.product.entitis.BienTheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IBienTheRepository extends JpaRepository<BienTheEntity,Long>, JpaSpecificationExecutor<BienTheEntity> {
}
