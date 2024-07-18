package com.shoescms.domain.stats;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.domain.payment.dtos.ETrangThaiDonHang;
import com.shoescms.domain.payment.repositories.IDonHangRepository;
import com.shoescms.domain.stats.dtos.StatsProductRevenueDto;
import com.shoescms.domain.stats.dtos.StatsRevenueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.TypeInformation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.shoescms.common.model.QFileEntity.fileEntity;
import static com.shoescms.domain.payment.entities.QChiTietDonHangEntity.chiTietDonHangEntity;
import static com.shoescms.domain.payment.entities.QDonHangEntity.donHangEntity;
import static com.shoescms.domain.product.entitis.QSanPhamEntity.sanPhamEntity;

@Service
public class ThongKeService {
    private final IDonHangRepository donHangRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public ThongKeService(IDonHangRepository donHangRepository, JPAQueryFactory jpaQueryFactory) {
        this.donHangRepository = donHangRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Object doanhThuTheoNgay(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(donHangEntity.ngayXoa.isNull());
        builder.and(donHangEntity.ngayTao.between(LocalDateTime.parse(startDate),LocalDateTime.parse(endDate)));

        return jpaQueryFactory.select(
                        Projections.constructor(StatsRevenueDto.class,
                donHangEntity.ngayTao.as("time"),
                                donHangEntity.trangThai
                                        .when(ETrangThaiDonHang.COMPLETED)
                                        .then(donHangEntity.tongTienSP)
                                        .otherwise(new BigDecimal(0))
                                        .sum()
                                        .castToNum(BigDecimal.class).as("total"),
                                donHangEntity.trangThai
                                        .when(ETrangThaiDonHang.PHONE_RETURNED)
                                        .then(donHangEntity.tongTienSP)
                                        .when(ETrangThaiDonHang.WRONG_SP_RETURNED)
                                        .then(donHangEntity.tongTienSP)
                                        .otherwise(new BigDecimal(0))
                                        .sum()
                                        .castToNum(BigDecimal.class).as("returned")        ))
                .from(donHangEntity)
                .where(builder)
                .orderBy(donHangEntity.ngayTao.asc())
                .groupBy(donHangEntity.ngayTao.year(),
                        donHangEntity.ngayTao.month(),
                        donHangEntity.ngayTao.dayOfMonth())
                .fetch();
    }

    public Object doanhThuHomNay(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(donHangEntity.ngayXoa.isNull());
        builder.and(donHangEntity.ngayTao.between(LocalDateTime.parse(startDate),LocalDateTime.parse(endDate)));


        return jpaQueryFactory.select(
                        Projections.constructor(StatsRevenueDto.class,
                                donHangEntity.ngayTao.hour().as("time"),
                                donHangEntity.trangThai
                                        .when(ETrangThaiDonHang.COMPLETED)
                                        .then(donHangEntity.tongTienSP)
                                        .otherwise(new BigDecimal(0))
                                        .sum()
                                        .castToNum(BigDecimal.class).as("total"),
                                donHangEntity.trangThai
                                        .when(ETrangThaiDonHang.PHONE_RETURNED)
                                        .then(donHangEntity.tongTienSP)
                                        .when(ETrangThaiDonHang.WRONG_SP_RETURNED)
                                        .then(donHangEntity.tongTienSP)
                                        .otherwise(new BigDecimal(0))
                                        .sum()
                                        .castToNum(BigDecimal.class).as("returned")
                        ))
                .from(donHangEntity)
                .where(builder)
                .orderBy(donHangEntity.ngayTao.asc())
                .groupBy(donHangEntity.ngayTao.year(),
                        donHangEntity.ngayTao.month(),
                        donHangEntity.ngayTao.dayOfMonth(),
                        donHangEntity.ngayTao.hour())
                .fetch();
    }

    public Object dashboard(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(donHangEntity.ngayXoa.isNull());
        builder.and(donHangEntity.ngayTao.between(LocalDateTime.parse(startDate),LocalDateTime.parse(endDate)));

        Map<Object, Object> map = new HashMap<>();
        jpaQueryFactory.select(
                        donHangEntity.trangThai,
                donHangEntity.id.count().as("total"))
                .from(donHangEntity)
                .where(builder,donHangEntity.trangThai.in(ETrangThaiDonHang.WAITING_CONFIRM, ETrangThaiDonHang.DELIVERING, ETrangThaiDonHang.COMPLETED))
                .groupBy(donHangEntity.trangThai)
                .fetch()
                .forEach(tuple -> {
                    map.put(tuple.get(donHangEntity.trangThai), tuple.get(1, Long.class));
                });

        map.put("total", Optional
                .ofNullable(jpaQueryFactory.select(donHangEntity.tongTienSP.sum())
                .from(donHangEntity)
                .where(builder, donHangEntity.trangThai.eq(ETrangThaiDonHang.COMPLETED))
                .fetchOne())
                .orElse(new BigDecimal(0)));
        return map;
    }

    public Page<StatsProductRevenueDto> doanhThuTungSanPham(String startDate, String endDate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(donHangEntity.ngayXoa.isNull());
        builder.and(donHangEntity.trangThai.eq(ETrangThaiDonHang.COMPLETED));
        builder.and(donHangEntity.ngayTao.between(LocalDateTime.parse(startDate),LocalDateTime.parse(endDate)));

        NumberExpression t = chiTietDonHangEntity.soLuong.multiply(chiTietDonHangEntity.giaTien).sum().castToNum(BigDecimal.class);
        JPAQuery<StatsProductRevenueDto> query = jpaQueryFactory.select(
                Projections.constructor(StatsProductRevenueDto.class,
                        sanPhamEntity.id,
                        sanPhamEntity.tieuDe,
                        ExpressionUtils.as(JPAExpressions.select(fileEntity.url).from(fileEntity).where(fileEntity.id.eq(sanPhamEntity.anhChinh)), "anhChinh"),
                        chiTietDonHangEntity.soLuong.sum(),
                        t
                        )
        )
                .from(chiTietDonHangEntity)
                .join(sanPhamEntity)
                .on(sanPhamEntity.id.eq(chiTietDonHangEntity.spId))
                .join(donHangEntity)
                .on(donHangEntity.id.eq(chiTietDonHangEntity.donHang))
                .where(builder)
                .groupBy(sanPhamEntity.id)
                .orderBy(t.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
