package com.shoescms.domain.product.repository;

import com.shoescms.domain.product.entitis.BienTheGiaTri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IBienTheGiaTriRepository extends JpaRepository<BienTheGiaTri,Long> , JpaSpecificationExecutor<BienTheGiaTri> {
}
