package com.shoescms.domain.user.repository;

import com.shoescms.common.config.QueryDSLConfig;
import com.shoescms.common.enums.RoleEnum;
import com.shoescms.domain.user.dto.UserDetailResDto;
import com.shoescms.domain.user.dto.UserResDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.shoescms.domain.auth.entity.QVaiTroEntity.vaiTroEntity;
import static com.shoescms.domain.user.entity.QNguoiDungEntity.nguoiDungEntity;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final QueryDSLConfig queryDSLConfig;
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<UserDetailResDto> getDetail(Long id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(UserDetailResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        vaiTroEntity.roleCd.as("roleCd"),
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
//                .where(nguoiDungEntity.id.eq(id))
                .fetchOne());
    }

    public Page<UserResDto> findStaffUserList(Long loggedUserId, String username, String name, String phone, String email, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (username != null)         builder.and(nguoiDungEntity.userName.eq(username));
        if (name != null)           builder.and(nguoiDungEntity.name.contains(name));
        if (phone != null)          builder.and(nguoiDungEntity.phone.contains(phone));
        if (email != null)          builder.and(nguoiDungEntity.email.contains(email));
        builder.and(nguoiDungEntity.role.roleCd.in("ROLE_ADMIN", "ROLE_STAFF"));
        builder.and(nguoiDungEntity.del.isFalse());
        builder.and(nguoiDungEntity.id.ne(loggedUserId));

        List<UserResDto> content = jpaQueryFactory
                .select(Projections.constructor(UserResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        nguoiDungEntity.role.roleCd,
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity.sex,
                        nguoiDungEntity.birthDate,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
                .where(builder)
                .orderBy(nguoiDungEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(nguoiDungEntity)
                .where(builder)
                .fetch().get(0);

        return new PageImpl<>(content, pageable, total);
    }

    public Page<UserResDto> findStoreUserList(Long loggedUserId, String userId, String name, String phone, String email, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (userId != null)     builder.and(nguoiDungEntity.userName.eq(userId));
        if (name != null)       builder.and(nguoiDungEntity.name.contains(name));
        if (phone != null)      builder.and(nguoiDungEntity.phone.contains(phone));
        if (email != null)      builder.and(nguoiDungEntity.email.contains(email));
        builder.and(nguoiDungEntity.del.isFalse());
        builder.and(nguoiDungEntity.role.roleCd.eq("ROLE_USER"));
        builder.and(nguoiDungEntity.id.ne(loggedUserId));

        List<UserResDto> content = jpaQueryFactory
                .select(Projections.constructor(UserResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        nguoiDungEntity.role.roleCd,
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity.sex,
                        nguoiDungEntity.birthDate,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
                .where(builder)
                .orderBy(nguoiDungEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(nguoiDungEntity)
                .where(builder)
                .fetch().get(0);

        return new PageImpl<>(content, pageable, total);
    }

    public List<UserResDto> exportStaffUserList(String string) {
        return jpaQueryFactory
                .select(Projections.constructor(UserResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        vaiTroEntity.roleCd.as("roleCd"),
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity.approved,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
                .join(vaiTroEntity)
                .on(vaiTroEntity.roleCd.contains("ROLE_STORE").not())
                .where(nguoiDungEntity.del.isFalse())
                .orderBy(nguoiDungEntity._super.ngayTao.desc())
                .fetch();
    }

    public List<UserResDto> exportStoreUserList(String string) {
        return jpaQueryFactory
                .select(Projections.constructor(UserResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        vaiTroEntity.roleCd.as("roleCd"),
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity.approved,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
                .join(vaiTroEntity)
                .on(vaiTroEntity.roleCd.contains("ROLE_STORE"))
                .where(nguoiDungEntity.del.isFalse())
                .orderBy(nguoiDungEntity._super.ngayTao.desc())
                .fetch();
    }

    public List<UserResDto> getUserForStoreMapping(String userId, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (userId != null)     builder.or(nguoiDungEntity.userName.contains(userId));
        if (name != null)       builder.or(nguoiDungEntity.name.contains(name));

        return jpaQueryFactory
                .select(Projections.constructor(UserResDto.class,
                        nguoiDungEntity.id,
                        nguoiDungEntity.userName,
                        nguoiDungEntity.name,
                        vaiTroEntity.roleCd.as("roleCd"),
                        nguoiDungEntity.phone,
                        nguoiDungEntity.email,
                        nguoiDungEntity.approved,
                        nguoiDungEntity._super.ngayTao
                ))
                .from(nguoiDungEntity)
//                .join(roleEntity) // task
//                .on(nguoiDungEntity)
                .where(builder)
                .fetch();
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createDate" -> {
                        OrderSpecifier<?> orderUserId = queryDSLConfig.getSortedColumn(direction, nguoiDungEntity, "createDate");
                        orders.add(orderUserId);
                    }
                    case "userId" -> {
                        OrderSpecifier<?> orderUserId = queryDSLConfig.getSortedColumn(direction, nguoiDungEntity, "userId");
                        orders.add(orderUserId);
                    }
                    case "name" -> {
                        OrderSpecifier<?> orderName = queryDSLConfig.getSortedColumn(direction, nguoiDungEntity, "name");
                        orders.add(orderName);
                    }
                    case "department" -> {
                        OrderSpecifier<?> orderDepartment = queryDSLConfig.getSortedColumn(direction, nguoiDungEntity, "department");
                        orders.add(orderDepartment);
                    }
                    default -> {
                    }
                }
            }
        }
        return orders;
    }
}
