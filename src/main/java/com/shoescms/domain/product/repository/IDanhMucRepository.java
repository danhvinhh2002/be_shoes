package com.shoescms.domain.product.repository;

import com.shoescms.domain.product.entitis.DMGiayEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IDanhMucRepository extends JpaRepository<DMGiayEntity, Long> , JpaSpecificationExecutor<DMGiayEntity>{


}
