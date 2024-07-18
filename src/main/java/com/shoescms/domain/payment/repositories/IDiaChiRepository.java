package com.shoescms.domain.payment.repositories;

import com.shoescms.domain.payment.entities.DiaChiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDiaChiRepository extends JpaRepository<DiaChiEntity, Long>, JpaSpecificationExecutor<DiaChiEntity> {
}
