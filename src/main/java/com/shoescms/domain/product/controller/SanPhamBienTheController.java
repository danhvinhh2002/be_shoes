package com.shoescms.domain.product.controller;


import com.shoescms.domain.product.dto.ResponseDto;
import com.shoescms.domain.product.entitis.BienTheEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import com.shoescms.domain.product.models.SanPhamBienTheModel;
import com.shoescms.domain.product.entitis.*;
import com.shoescms.domain.product.repository.ISanPhamBienTheRepository;
import com.shoescms.domain.product.service.impl.ISanPhamBienTheServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "03.2. Phan loai SP giay")
@RestController
@RequestMapping("/v1/san-pham-bien-the")

public class SanPhamBienTheController {

    @Autowired
    ISanPhamBienTheRepository sanPhamBienTheRepository;
    @Autowired
    ISanPhamBienTheServiceImpl iSanPhamBienTheService;

    @PostMapping("/search")
    public ResponseDto search(@RequestBody SanPhamBienTheModel model, Pageable pageable) {
        List<Specification<SanPhamBienTheEntity>> listSpect = new ArrayList<>();
        if (model.getSanPham() != null) {
            listSpect.add((root, query, criteriaBuilder) -> {
                        Join<SanPhamBienTheEntity, SanPhamEntity> join = root.join(SanPhamBienTheEntity_.SAN_PHAM);
                        return criteriaBuilder.equal(join.get(SanPhamEntity_.ID), model.getSanPham());
                    }
            );

        }
        if (model.getBienThe1() != null) {

            listSpect.add((root, query, criteriaBuilder) ->
                    {
                        Join<SanPhamBienTheEntity, BienTheEntity> join = root.join(SanPhamBienTheEntity_.BIEN_THE1);
                        return criteriaBuilder.equal(join.get(BienTheEntity_.ID), model.getBienThe1());
                    }
            );

        }
        if (model.getBienThe2() != null) {
            listSpect.add((root, query, criteriaBuilder) -> {
                        Join<SanPhamBienTheEntity, BienTheEntity> join = root.join(SanPhamBienTheEntity_.BIEN_THE2);

                        return criteriaBuilder.equal(join.get(BienTheEntity_.ID), model.getBienThe2());
                    }
            );
        }

        listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(SanPhamBienTheEntity_.NGAY_XOA)));
        Specification<SanPhamBienTheEntity> finalSpect = null;
        for (Specification spec : listSpect) {
            if (finalSpect == null) {
                finalSpect = Specification.where(spec);
            } else {
                finalSpect = finalSpect.and(spec);
            }
        }
        return ResponseDto.of(iSanPhamBienTheService.filterEntities(pageable, finalSpect));
    }


}
