package com.shoescms.domain.auth.repository;

import com.shoescms.domain.auth.entity.VaiTroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVaiTroRepository extends JpaRepository<VaiTroEntity, Long> {
    VaiTroEntity findByRoleCd(String roleEnum);
}