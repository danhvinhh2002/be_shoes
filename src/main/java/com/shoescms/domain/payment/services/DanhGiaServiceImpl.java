package com.shoescms.domain.payment.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.domain.payment.dtos.DanhGiaDto;
import com.shoescms.domain.payment.dtos.DanhGiaReqDTO;
import com.shoescms.domain.payment.entities.DanhGiaEntity;
import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.payment.repositories.IDanhGiaRepository;
import com.shoescms.domain.payment.repositories.IDonHangRepository;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.repository.ISanPhamRepository;
import com.shoescms.domain.user.dto.UsermetaDto;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.shoescms.domain.payment.entities.QChiTietDonHangEntity.chiTietDonHangEntity;
import static com.shoescms.domain.payment.entities.QDanhGiaEntity.danhGiaEntity;

@Service
public class DanhGiaServiceImpl  {

    @Autowired
    private IDanhGiaRepository repo;

    @Autowired
    private ISanPhamRepository sanPhamRepository;

    @Autowired
    private IDonHangRepository donHangRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private INguoiDungRepository userRepository;

    @Transactional
    public DanhGiaDto create(DanhGiaReqDTO danhGia) {
        // Thêm mới đánh giá và cập nhật sản phẩm
        DanhGiaEntity entity = DanhGiaEntity.builder()
                .donHangChiTietId(danhGia.getDonHangChiTietId())
                .binhLuan(danhGia.getBinhLuan())
                .ngayTao(danhGia.getNgayTao())
                .nguoiTaoId(danhGia.getNguoiTaoId())
                .soSao(danhGia.getSoSao())
                .isHide(false)
                .build();
        repo.save(entity);

        // Cập nhật điểm trung bình đánh giá cho sản phẩm
        SanPhamEntity sp = repo.findSanPhamDanhGia(danhGia.getDonHangChiTietId());
        DonHangEntity dh = repo.findDonHangDanhGia(danhGia.getDonHangChiTietId());

        System.out.println("sp = " + sp);
        System.out.println("dh = " + dh);
        if (sp != null) {
            System.out.println("sanPham = " + sp);
            Long soNguoiDanhGia = layDanhGiaChoSp(sp.getId() , null,  PageRequest.of(0,1)).getTotalElements();
            Double soSao = repo.findRatingBySanPham(sp.getId());
            sp.setTbDanhGia(soSao.floatValue()); // Đảm bảo không gặp vấn đề với giá trị null
            sp.setSoDanhGia(soNguoiDanhGia.intValue());
            dh.setCheckRate(1);
            sanPhamRepository.save(sp);
            donHangRepository.save(dh);
            System.out.println("sanPham sau khi update = " + sp);
        } else {
            // Xử lý trường hợp không tìm thấy sản phẩm
            // Có thể làm gì đó tùy thuộc vào yêu cầu của bạn
        }

        return DanhGiaDto.toDto(entity);
    }


    public List<DanhGiaEntity> findByIds(List<Long> ids) {
        return repo.findByIds(ids);
    }

    public Page<DanhGiaDto> layDanhGiaChoSp(Long id, String q, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chiTietDonHangEntity.spId.eq(id));
        if(!ObjectUtils.isEmpty(q))
            builder.and(danhGiaEntity.binhLuan.contains(q));

        JPAQuery<DanhGiaEntity> jpaQuery = jpaQueryFactory.selectFrom(danhGiaEntity)
                .join(chiTietDonHangEntity)
                .on(chiTietDonHangEntity.id.eq(danhGiaEntity.donHangChiTietId))
                .where(builder)
                .orderBy(danhGiaEntity.ngayTao.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<DanhGiaDto> content = jpaQuery
                .fetch()
                .stream()
                .map(DanhGiaDto::toDto)
                .peek(item -> item.setNguoiTao(UsermetaDto.toDto(userRepository.findById(item.getNguoiTaoId()).orElse(null))))
                .toList();
        return new PageImpl<>(content, pageable, jpaQuery.fetchCount());
    }

    @Transactional
    public void xoaDanhGia(Long id) {
        try{
            DanhGiaEntity danhGia = repo.findById(id).orElse(null);
            if(danhGia!= null) {
                repo.deleteById(danhGia.getId());
                SanPhamEntity sp = repo.findSanPhamDanhGia(danhGia.getDonHangChiTietId());
                if (sp != null) {
                    Long soNguoiDanhGia = layDanhGiaChoSp(sp.getId(), null, PageRequest.of(0,1)).getTotalElements();
                    Double soSao = repo.findRatingBySanPham(sp.getId());
                    sp.setTbDanhGia(soSao.floatValue()); // Đảm bảo không gặp vấn đề với giá trị null
                    sp.setSoDanhGia(soNguoiDanhGia.intValue());
                    sanPhamRepository.save(sp);
                }
            }
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public Double findRatingBySanPham(Long idSanPham) {
        return repo.findRatingBySanPham(idSanPham);
    }

    public void anDanhGia(Long id) {
        DanhGiaEntity danhGia = repo.findById(id).orElse(null);
        if(danhGia!= null) {
            danhGia.setIsHide(true);
            repo.saveAndFlush(danhGia);
        }
    }
}
