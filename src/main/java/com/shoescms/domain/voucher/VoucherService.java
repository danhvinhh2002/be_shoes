package com.shoescms.domain.voucher;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.domain.user.dto.UsermetaDto;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import com.shoescms.domain.voucher.dto.VoucherDto;
import com.shoescms.domain.voucher.dto.VoucherMetadataDto;
import com.shoescms.domain.voucher.dto.VoucherReqDto;
import com.shoescms.domain.voucher.entity.ELoaiGiamGia;
import com.shoescms.domain.voucher.entity.VoucherEntity;
import com.shoescms.domain.voucher.repository.IVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.shoescms.domain.product.entitis.QSanPhamEntity.sanPhamEntity;
import static com.shoescms.domain.voucher.entity.QVoucherEntity.voucherEntity;

@Component
@RequiredArgsConstructor
public class VoucherService {

    private final IVoucherRepository voucherRepository;
    private final INguoiDungRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public void deleteByIds(List<Long> ids) {
        List<VoucherEntity> voucherEntities = voucherRepository.findAllById(ids);
        if (voucherEntities.size() > 0) {
            voucherEntities.forEach(i -> i.setNgayXoa(LocalDateTime.now()));
            voucherRepository.saveAllAndFlush(voucherEntities);
        }
    }

    @Transactional
    public VoucherDto update(VoucherReqDto reqDto, long loggedUserId) {
        VoucherEntity original = getById(reqDto.getId());
        VoucherEntity entity = reqDto.toEntity();
        entity.setNgayTao(original.getNgayTao());
        entity.setNguoiTao(original.getNguoiTao());
        entity.setNguoiCapNhat(loggedUserId);
        voucherRepository.saveAndFlush(entity);
        return VoucherDto.toDto(entity);
    }

    @Transactional
    public VoucherDto add(VoucherReqDto reqDto, long loggedUserId) {
        VoucherEntity entity = reqDto.toEntity();
        entity.setNguoiTao(loggedUserId);
        entity.setNguoiCapNhat(loggedUserId);
        voucherRepository.saveAndFlush(entity);
        return VoucherDto.toDto(entity);
    }

    public VoucherDto findById(Long id) {
        VoucherEntity entity = getById(id);
        if (entity.getNgayXoa() != null) throw new ObjectNotFoundException(-99);
        VoucherDto dto = VoucherDto.toDto(entity);
        dto.setNguoiTao(UsermetaDto.toDto(userRepository.findById(entity.getNguoiTao()).orElse(null)));
        dto.setNguoiCapNhat(UsermetaDto.toDto(userRepository.findById(entity.getNguoiCapNhat()).orElse(null)));
        return dto;
    }
    public VoucherEntity getById(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(-99));
    }

    public Page<VoucherDto> filter(Pageable pageable, Specification<VoucherEntity> where) {
        return voucherRepository.findAll(where, pageable)
                .map(entity -> {
                    VoucherDto dto = VoucherDto.toDto(entity);
                    dto.setNguoiTao(UsermetaDto.toDto(userRepository.findById(entity.getNguoiTao()).orElse(null)));
                    dto.setNguoiCapNhat(UsermetaDto.toDto(userRepository.findById(entity.getNguoiCapNhat()).orElse(null)));
                    return dto;
                });
    }

    public List<VoucherMetadataDto> findAvailableVoucherByDanhMuc(Long danhMucId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(voucherEntity.danhMucList.contains(danhMucId.toString())
                .or(voucherEntity.loaiGiamGia.eq(ELoaiGiamGia.ALL)));
        builder.and(voucherEntity.ngayXoa.isNull());
        LocalDate currentDate = LocalDate.now();
        builder.and(voucherEntity.ngayBatDau.loe(currentDate));
        builder.and(voucherEntity.ngayKetThuc.goe(currentDate));
        return jpaQueryFactory.selectDistinct(voucherEntity)
                .from(voucherEntity)
                .where(builder)
                .fetch()
                .stream()
                .map(VoucherMetadataDto::toDto)
                .toList();
    }

    public VoucherDto checkVoucher(String code, List<Long> productList) {
        VoucherEntity entity = voucherRepository.findByMaGiamGia(code).orElseThrow(() -> new ObjectNotFoundException(-99));
        if (entity.getNgayXoa() != null) throw new ObjectNotFoundException(-99);
        VoucherDto dto = VoucherDto.toDto(entity);

        List<Long> dmList = jpaQueryFactory.selectDistinct(sanPhamEntity.dmGiay.id)
                .from(sanPhamEntity)
                .where(sanPhamEntity.ngayXoa.isNull(), sanPhamEntity.id.in(productList))
                .fetch();

        if (entity.getLoaiGiamGia().equals(ELoaiGiamGia.BY_CATEGORY)) {
            List<Long> appliedDmList = Arrays.stream(entity.getDanhMucList().split(",")).map(Long::valueOf).toList();
            for (Long dmId : dmList)
                if (!appliedDmList.contains(dmId))
                    throw new ObjectNotFoundException(-99);
        }
        return dto;
    }

    @Transactional
    public void updateEntity(VoucherEntity voucherEntity) {
        voucherRepository.saveAndFlush(voucherEntity);
    }
}
