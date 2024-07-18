package com.shoescms.domain.product.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.common.utils.ASCIIConverter;
import com.shoescms.domain.product.dto.DanhMucDTO;
import com.shoescms.domain.product.entitis.DMGiayEntity;
import com.shoescms.domain.product.entitis.QDMGiayEntity;
import com.shoescms.domain.product.entitis.QSanPhamEntity;
import com.shoescms.domain.product.models.DanhMucGiayModel;
import com.shoescms.domain.product.repository.IDanhMucRepository;
import com.shoescms.domain.product.service.IDanhMucGiayService;
import com.shoescms.domain.product.service.ISanPhamService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class IDMGiayServiceImpl implements IDanhMucGiayService {

    @Autowired
    @Lazy
    IDanhMucRepository danhMucRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private ISanPhamService sanPhamService;

    @Override
    public Page<DMGiayEntity> filterEntities(Pageable pageable, Specification<DMGiayEntity> specification) {
        return this.danhMucRepository.findAll(specification, pageable);
    }

    @Override
    public DanhMucDTO add(DanhMucGiayModel danhMucGiayModel) {
        DMGiayEntity dmGiayEntity = DMGiayEntity.builder()
                .tenDanhMuc(danhMucGiayModel.getTenDanhMuc())
                .slug(ASCIIConverter.utf8ToAscii(danhMucGiayModel.getTenDanhMuc()))
                .build();
        danhMucRepository.saveAndFlush(dmGiayEntity);
        return DanhMucDTO.toDTO(dmGiayEntity);

    }

    @Override
    public DanhMucDTO update(DanhMucGiayModel danhMucGiayModel) {
        DMGiayEntity dmGiayEntity = danhMucRepository.findById(danhMucGiayModel.getId()).orElse(null);
        if (dmGiayEntity != null) {
            dmGiayEntity.setTenDanhMuc(danhMucGiayModel.getTenDanhMuc());
            dmGiayEntity.setSlug(ASCIIConverter.utf8ToAscii(danhMucGiayModel.getTenDanhMuc()));
            danhMucRepository.saveAndFlush(dmGiayEntity);

        }
        return DanhMucDTO.toDTO(dmGiayEntity);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            DMGiayEntity entity = this.getById(id);
            entity.setNgayXoa(LocalDateTime.now());
            this.danhMucRepository.saveAndFlush(entity);
            sanPhamService.setMacDinhDanhMuc(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<DanhMucDTO> getDanhMucs(String tenDanhMuc, String slug, Pageable pageable) {
        List<DMGiayEntity> dmGiayEntities = danhMucRepository.findAll((Specification<DMGiayEntity>) (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();
            p = criteriaBuilder.and(p, criteriaBuilder.notEqual(root.get("id"), 1L));
            if (!StringUtils.isEmpty(tenDanhMuc)) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("tenDanhMuc"), "%" + tenDanhMuc + "%"));
            }
            if (!StringUtils.isEmpty(slug)) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("slug"), "%" + slug + "%"));
            }
            query.orderBy(criteriaBuilder.desc(root.get("tenDanhMuc")), criteriaBuilder.asc(root.get("id")));
            return p;
        }, pageable).getContent();
        return DanhMucDTO.convertToTDO(dmGiayEntities);
    }

    @Override
    public Page<DanhMucDTO> locDanhMuc(String tenDanhMuc, String layMacDinh, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(tenDanhMuc))
            builder.and(QDMGiayEntity.dMGiayEntity.tenDanhMuc.contains(tenDanhMuc));

        builder.and(QDMGiayEntity.dMGiayEntity.id.ne(1L));
        builder.and(QDMGiayEntity.dMGiayEntity.ngayXoa.isNull());
        List<DanhMucDTO> content = jpaQueryFactory
                .selectDistinct(
                        QDMGiayEntity.dMGiayEntity.id,
                        QDMGiayEntity.dMGiayEntity.tenDanhMuc,
                        QDMGiayEntity.dMGiayEntity.slug,
                        QDMGiayEntity.dMGiayEntity.ngayTao,
                        ExpressionUtils.as(jpaQueryFactory.select(QSanPhamEntity.sanPhamEntity.id.countDistinct().castToNum(Integer.class)).from(QSanPhamEntity.sanPhamEntity).where(QSanPhamEntity.sanPhamEntity.dmGiay.id.eq(QDMGiayEntity.dMGiayEntity.id)), "soSp")
                )
                .from(QDMGiayEntity.dMGiayEntity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QDMGiayEntity.dMGiayEntity.id.desc())
                .fetch()
                .stream()
                .map(tuple -> DanhMucDTO
                        .builder()
                        .id(tuple.get(QDMGiayEntity.dMGiayEntity.id))
                        .tenDanhMuc(tuple.get(QDMGiayEntity.dMGiayEntity.tenDanhMuc))
                        .slug(tuple.get(QDMGiayEntity.dMGiayEntity.slug))
                        .ngayTao(tuple.get(QDMGiayEntity.dMGiayEntity.ngayTao))
                        .soSp(tuple.get(4, Integer.class))
                        .build())
                .toList();

        Long total = Optional.ofNullable(
                jpaQueryFactory.select(QDMGiayEntity.dMGiayEntity.id.countDistinct())
                        .from(QDMGiayEntity.dMGiayEntity)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    public DMGiayEntity getById(Long id) {
        return danhMucRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }
}
