package com.shoescms.domain.cart.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.common.model.FileEntity;
import com.shoescms.common.model.repositories.FileRepository;
import com.shoescms.domain.cart.dto.GioHangChiTietDto;
import com.shoescms.domain.cart.dto.GioHangChiTietResDto;
import com.shoescms.domain.cart.dto.GioHangResDto;
import com.shoescms.domain.cart.entity.GioHangEntity;
import com.shoescms.domain.cart.entity.GioHangChiTiet;
import com.shoescms.domain.cart.entity.GioHangChiTiet_;
import com.shoescms.domain.cart.model.GioHangChiTietModel;
import com.shoescms.domain.cart.repository.IGioHangChiTietRepository;
import com.shoescms.domain.cart.repository.IGioHangRepository;
import com.shoescms.domain.cart.service.GioHangService;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import com.shoescms.domain.product.repository.IBienTheGiaTriRepository;
import com.shoescms.domain.product.repository.ISanPhamBienTheRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.shoescms.domain.cart.entity.QGioHangEntity.gioHangEntity;
import static com.shoescms.domain.cart.entity.QGioHangChiTiet.gioHangChiTiet;

@Service
public class GioHangServiceImpl implements GioHangService {

    private final IGioHangRepository gioHangRepository;
    private final IGioHangChiTietRepository gioHangChiTietRepository;
    private final ISanPhamBienTheRepository sanPhamBienTheRepository;

    private final IBienTheGiaTriRepository bienTheGiaTriRepository;
    private final FileRepository fileRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public GioHangServiceImpl(IGioHangRepository gioHangRepository, IGioHangChiTietRepository gioHangChiTietRepository, ISanPhamBienTheRepository sanPhamBienTheRepository, IBienTheGiaTriRepository bienTheGiaTriRepository, FileRepository fileRepository, JPAQueryFactory jpaQueryFactory) {
        this.gioHangRepository = gioHangRepository;
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.sanPhamBienTheRepository = sanPhamBienTheRepository;
        this.bienTheGiaTriRepository = bienTheGiaTriRepository;
        this.fileRepository = fileRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public GioHangResDto findById(Long id) {
        GioHangEntity entity = this.gioHangRepository.findById(id).orElse(null);
        if (entity == null)
            return null;

        return GioHangResDto.class.cast(entity);
    }

    @Override
    public GioHangEntity findCartByUserId(Long nguoiDungEntity) {
        return gioHangRepository.findByNguoiDungId(nguoiDungEntity);
    }

    @Transactional
    @Override
    public List<GioHangChiTietResDto> gioHangCuaToi(Long nguoiDungEntity) {
        List<GioHangChiTietResDto> gioHangChiTietResDtos = new ArrayList<>();
        GioHangEntity gioHang = gioHangRepository.findByNguoiDungId(nguoiDungEntity);
        if (gioHang != null)
            gioHangChiTietRepository.findAllByGioHang(gioHang.getId(), Sort.by(Sort.Order.desc(GioHangChiTiet_.NGAY_CAP_NHAT)))
                    .forEach(item ->
                    {
                        SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(item.getSanPhamBienThe()).orElse(null);
                        if (sanPhamBienTheEntity.getNgayXoa() == null) {
                            if (item.getSoLuong() > sanPhamBienTheEntity.getSoLuong()) {
                                item.setSoLuong(sanPhamBienTheEntity.getSoLuong());
                                gioHangChiTietRepository.saveAndFlush(item);
                            }

                            FileEntity anh = null;
                            if (sanPhamBienTheEntity.getAnh() != null)
                                anh = fileRepository.findById(sanPhamBienTheEntity.getAnh()).orElse(null);
                            else if (sanPhamBienTheEntity.getSanPham().getAnhChinh() != null)
                                anh = fileRepository.findById(sanPhamBienTheEntity.getSanPham().getAnhChinh()).orElse(null);

                            GioHangChiTietResDto gioHangChiTietDto = GioHangChiTietResDto
                                    .builder()
                                    .id(sanPhamBienTheEntity.getId())
                                    .qty(item.getSoLuong())
                                    .productId(sanPhamBienTheEntity.getSanPham().getId())
                                    .productName(sanPhamBienTheEntity.getSanPham().getTieuDe())
                                    .price(sanPhamBienTheEntity.getSanPham().getGiaMoi())
                                    .variation(sanPhamBienTheEntity.getMotaPhanLoai())
                                    .stockCnt(sanPhamBienTheEntity.getSoLuong())
                                    .build();
                            if (anh != null)
                                gioHangChiTietDto.setAnh(anh.getUrl());
                            gioHangChiTietResDtos.add(gioHangChiTietDto);
                        }


                    });
        return gioHangChiTietResDtos;
    }

    @Transactional
    @Override
    public GioHangEntity add(GioHangEntity gioHang) {
        return gioHangRepository.saveAndFlush(gioHang);
    }

    @Transactional
    @Override
    public void remove(Long spBienTheId, Long userId) {
        GioHangChiTiet entity = jpaQueryFactory.selectFrom(gioHangChiTiet)
                .join(gioHangEntity)
                .on(gioHangEntity.id.eq(gioHangChiTiet.gioHang))
                .where(gioHangChiTiet.sanPhamBienThe.eq(spBienTheId),
                        gioHangEntity.nguoiDungId.eq(userId))
                .fetchOne();
        if (entity != null)
            this.gioHangChiTietRepository.delete(entity);
    }

    @Override
    public SanPhamBienTheEntity getBienTheBySanPhamId(Long sanPhamBienThe) {
        return this.sanPhamBienTheRepository.findById(sanPhamBienThe).orElseThrow(() -> new ObjectNotFoundException(50));
    }

    @Override
    public GioHangChiTietDto addItem(GioHangChiTietModel reqDto) {
        // Kiểm tra xem có sản phẩm biến thể đã tồn tại trong giỏ hàng chưa

        GioHangChiTiet existingEntity = this.gioHangChiTietRepository.findByGioHangAndSanPhamBienThe(
                reqDto.getGioHang(), reqDto.getSanPhamBienThe());

        if (existingEntity != null) {
            // Nếu sản phẩm biến thể đã tồn tại, thì tăng số lượng lên
            existingEntity.setSoLuong(existingEntity.getSoLuong() + reqDto.getSoLuong());
            this.gioHangChiTietRepository.saveAndFlush(existingEntity);
            return GioHangChiTietDto.toDto(existingEntity);
        } else {
            // Nếu sản phẩm biến thể chưa tồn tại, thì tạo mới
            GioHangChiTiet entity = GioHangChiTiet.builder()
                    .gioHang(reqDto.getGioHang())
                    .sanPhamBienThe(reqDto.getSanPhamBienThe())
                    .soLuong(reqDto.getSoLuong())
                    .build();
            this.gioHangChiTietRepository.saveAndFlush(entity);
            return GioHangChiTietDto.toDto(entity);
        }
    }


    @Transactional
    @Override
    public List<GioHangChiTietResDto> dongBoGioHang(List<GioHangChiTietModel> models, Long userId) {
        GioHangEntity gioHang = findCartByUserId(userId);
        if (gioHang == null)
            gioHang = add(GioHangEntity
                    .builder()
                    .nguoiDungId(userId)
                    .build());
        GioHangEntity finalGioHang = gioHang;
        if (!ObjectUtils.isEmpty(models))
            models.forEach(item -> {
                GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findByGioHangAndSanPhamBienThe(finalGioHang.getId(), item.getSanPhamBienThe());
                if (gioHangChiTiet == null)
                    gioHangChiTiet = GioHangChiTiet
                            .builder()
                            .gioHang(finalGioHang.getId())
                            .sanPhamBienThe(item.getSanPhamBienThe())
                            .soLuong(item.getSoLuong())
                            .build();
                SanPhamBienTheEntity sanPhamBienTheEntity = getBienTheBySanPhamId(item.getSanPhamBienThe());
                if (sanPhamBienTheEntity.getNgayXoa() == null) {
                    if (gioHangChiTiet.getSoLuong() > sanPhamBienTheEntity.getSoLuong())
                        gioHangChiTiet.setSoLuong(sanPhamBienTheEntity.getSoLuong());
                    gioHangChiTietRepository.saveAndFlush(gioHangChiTiet);
                }
            });
        return gioHangCuaToi(userId);
    }

}
