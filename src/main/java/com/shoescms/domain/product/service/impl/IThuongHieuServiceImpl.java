package com.shoescms.domain.product.service.impl;

import com.shoescms.common.utils.ASCIIConverter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.domain.product.dto.ThuongHieuDTO;
import com.shoescms.domain.product.entitis.QSanPhamEntity;
import com.shoescms.domain.product.entitis.QThuongHieuEntity;
import com.shoescms.domain.product.entitis.ThuongHieuEntity;
import com.shoescms.domain.product.models.ThuongHieuModel;
import com.shoescms.domain.product.repository.IThuogHieuRepository;
import com.shoescms.domain.product.service.ISanPhamService;
import com.shoescms.domain.product.service.IThuongHieuService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IThuongHieuServiceImpl implements IThuongHieuService {
    @Autowired
    IThuogHieuRepository thuogHieuRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private ISanPhamService sanPhamService;
    @Override
    public Page<ThuongHieuEntity> filterEntities(Pageable pageable, Specification<ThuongHieuEntity> specification) {
        return thuogHieuRepository.findAll(specification, pageable);
    }

    @Override
    public ThuongHieuDTO add(ThuongHieuModel thuongHieuModel) {
       ThuongHieuEntity thuongHieuEntity = ThuongHieuEntity.builder()
               .tenThuongHieu(thuongHieuModel.getTenThuongHieu())
               .slug(ASCIIConverter.utf8ToAscii(thuongHieuModel.getTenThuongHieu()))
               .build();
                thuogHieuRepository.save(thuongHieuEntity);
       return ThuongHieuDTO.toDTO(thuongHieuEntity);
    }

    @Override
    public ThuongHieuDTO update(ThuongHieuModel thuongHieuModel) {
        ThuongHieuEntity th = thuogHieuRepository.findById(thuongHieuModel.getId()).orElse(null);
        if (th != null) {
            th.setTenThuongHieu(thuongHieuModel.getTenThuongHieu());
            th.setSlug(ASCIIConverter.utf8ToAscii(thuongHieuModel.getTenThuongHieu()));
            thuogHieuRepository.save(th);
        }
        return ThuongHieuDTO.toDTO(th);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            ThuongHieuEntity entity = this.getById(id);
            entity.setNgayXoa(LocalDateTime.now());
            this.thuogHieuRepository.saveAndFlush(entity);
            sanPhamService.setMacDinhThuongHieu(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ThuongHieuDTO> getThuongHieus(String tenThuongHieu, String slug, Pageable pageable) {
        List<ThuongHieuEntity> thuongHieuEntity = thuogHieuRepository.findAll((Specification<ThuongHieuEntity>) (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(tenThuongHieu)) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("tenThuongHieu"), "%" + tenThuongHieu + "%"));
            }
            if (!StringUtils.isEmpty(slug)) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("slug"), "%" + slug + "%"));
            }
            query.orderBy(criteriaBuilder.desc(root.get("tenThuongHieu")), criteriaBuilder.asc(root.get("id")));
            return p;
        }, pageable).getContent();
        return ThuongHieuDTO.convertToTDO(thuongHieuEntity);
    }

    @Override
    public Page<ThuongHieuDTO> locThuongHieu(String tenThuongHieu, String layMacDinh,Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(tenThuongHieu))
            builder.and(QThuongHieuEntity.thuongHieuEntity.tenThuongHieu.contains(tenThuongHieu));
        builder.and(QThuongHieuEntity.thuongHieuEntity.id.ne(1L));

        builder.and(QThuongHieuEntity.thuongHieuEntity.ngayXoa.isNull());
        List<ThuongHieuDTO> content = jpaQueryFactory.selectDistinct(
                        QThuongHieuEntity.thuongHieuEntity.id,
                        QThuongHieuEntity.thuongHieuEntity.tenThuongHieu,
                        QThuongHieuEntity.thuongHieuEntity.slug,
                        QThuongHieuEntity.thuongHieuEntity.ngayTao,
                        ExpressionUtils.as(jpaQueryFactory.select(QSanPhamEntity.sanPhamEntity.id.countDistinct().castToNum(Integer.class)).from(QSanPhamEntity.sanPhamEntity).where(QSanPhamEntity.sanPhamEntity.thuongHieu.id.eq(QThuongHieuEntity.thuongHieuEntity.id)), "soSp")
                )
                .from(QThuongHieuEntity.thuongHieuEntity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QThuongHieuEntity.thuongHieuEntity.id.desc())
                .fetch()
                .stream()
                .map(tuple -> ThuongHieuDTO
                        .builder()
                        .id(tuple.get(QThuongHieuEntity.thuongHieuEntity.id))
                        .tenThuongHieu(tuple.get(QThuongHieuEntity.thuongHieuEntity.tenThuongHieu))
                        .slug(tuple.get(QThuongHieuEntity.thuongHieuEntity.slug))
                        .ngayTao(tuple.get(QThuongHieuEntity.thuongHieuEntity.ngayTao))
                        .soSp(tuple.get(4, Integer.class))
                        .build())
                .toList();

        Long total = Optional.ofNullable(
                jpaQueryFactory.select(QThuongHieuEntity.thuongHieuEntity.id.countDistinct())
                        .from(QThuongHieuEntity.thuongHieuEntity)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    public ThuongHieuEntity getById(Long id) {
        return thuogHieuRepository.findById(id).orElseThrow(() -> new RuntimeException("22"));
    }

}
