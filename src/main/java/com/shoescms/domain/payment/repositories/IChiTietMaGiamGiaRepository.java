package com.shoescms.domain.payment.repositories;

import com.shoescms.domain.payment.entities.ChiTietMaGiamGiaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IChiTietMaGiamGiaRepository extends JpaRepository<ChiTietMaGiamGiaEntity,Long>, JpaSpecificationExecutor<ChiTietMaGiamGiaEntity> {
}
