package com.shoescms.domain.user.repository;

import com.shoescms.domain.user.entity.NguoiDungEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INguoiDungRepository extends JpaRepository<NguoiDungEntity, Long> {
    Optional<NguoiDungEntity> findByUserNameAndDel(String userId, Boolean del);
    Optional<NguoiDungEntity> findByEmailAndDel(String email, Boolean del);
    Optional<NguoiDungEntity> findByPhoneAndDel(String phone, Boolean del);
    Optional<NguoiDungEntity> findByIdAndDel(Long userId, Boolean del);
    Optional<NguoiDungEntity> findByUserNameAndNameAndEmail(String userId, String name, String email);
    List<NguoiDungEntity> findByNameAndPhone(String name, String phone);
    NguoiDungEntity findByEmail(String email);
}
