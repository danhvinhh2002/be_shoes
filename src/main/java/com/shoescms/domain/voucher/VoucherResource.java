package com.shoescms.domain.voucher;

import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.voucher.dto.EVoucherStatus;
import com.shoescms.domain.voucher.dto.VoucherDto;
import com.shoescms.domain.voucher.dto.VoucherFilterReqDto;
import com.shoescms.domain.voucher.dto.VoucherReqDto;
import com.shoescms.domain.voucher.entity.VoucherEntity;
import com.shoescms.domain.voucher.entity.VoucherEntity_;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/voucher")
@RequiredArgsConstructor
public class VoucherResource {

    private final VoucherService voucherService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("filter")
    public Page<VoucherDto> filter(@RequestBody VoucherFilterReqDto reqDto, Pageable pageable) {
        List<Specification<VoucherEntity>> specs = new ArrayList<>();
        if(reqDto.getStatus() != null){
            specs.add((root, query, criteriaBuilder) -> {
                        LocalDate currentDate = LocalDate.now();
                        if (EVoucherStatus.USING == reqDto.getStatus())
                            return criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(root.get(VoucherEntity_.NGAY_BAT_DAU), currentDate),
                                    criteriaBuilder.greaterThanOrEqualTo(root.get(VoucherEntity_.NGAY_KET_THUC), currentDate)
                            );
                        else if (EVoucherStatus.COMING == reqDto.getStatus())
                            return criteriaBuilder.greaterThan(root.get(VoucherEntity_.NGAY_BAT_DAU), currentDate);
                        else
                            return criteriaBuilder.lessThan(root.get(VoucherEntity_.NGAY_KET_THUC), currentDate);

                    }
            );
        }
        specs.add((root, query, cb) -> cb.isNull(root.get(VoucherEntity_.NGAY_XOA)));

        Specification<VoucherEntity> finalSpec = null;
        for(Specification<VoucherEntity> spec: specs)
            if(finalSpec == null) finalSpec = Specification.where(spec);
            else finalSpec.and(spec);
        return voucherService.filter(pageable, finalSpec);
    }

    @GetMapping("{id}")
    public VoucherDto findById(@PathVariable Long id) {
        return voucherService.findById(id);
    }

    @PostMapping
    public VoucherDto add(@RequestBody VoucherReqDto reqDto,  @RequestHeader(name = "x-api-token") String xApiToken) {
        reqDto.setId(null);
        return voucherService.add(reqDto, Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
    }



    @PutMapping("{id}")
    public VoucherDto update(@PathVariable(name = "id") Long id, @RequestBody VoucherReqDto reqDto,  @RequestHeader(name = "x-api-token", required = false) String xApiToken) {
        reqDto.setId(id);
        return voucherService.update(reqDto, Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
    }

    @DeleteMapping("bulk")
    public void delete(@RequestParam List<Long> ids) {
        this.voucherService.deleteByIds(ids);
    }


    @GetMapping("public/kiem-tra-voucher/{code}")
    public VoucherDto checkVoucher(@PathVariable String code, @RequestParam List<Long> productList){
        return voucherService.checkVoucher(code, productList);
    }
}
