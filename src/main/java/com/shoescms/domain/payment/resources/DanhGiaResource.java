package com.shoescms.domain.payment.resources;

import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.payment.dtos.DanhGiaDto;
import com.shoescms.domain.payment.dtos.DanhGiaReqDTO;
import com.shoescms.domain.payment.entities.DanhGiaEntity;
import com.shoescms.domain.payment.services.DanhGiaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/danh-gia")
public class DanhGiaResource {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private DanhGiaServiceImpl service;

    @PostMapping("/create")
    public List<DanhGiaDto> create(@RequestBody List<DanhGiaReqDTO> danhGiaList, @RequestHeader(name = "x-api-token", required = false) String xApiToken) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(xApiToken));
        List<DanhGiaDto> savedDanhGiaList = new ArrayList<>();
        for (DanhGiaReqDTO danhGia : danhGiaList) {
            danhGia.setNguoiTaoId(userId);
            danhGia.setNgayTao(LocalDateTime.now());
            savedDanhGiaList.add(service.create(danhGia));
        }

        return savedDanhGiaList;
    }

    @GetMapping("/byIds")
    public ResponseEntity<List<DanhGiaEntity>> searchByIds(@RequestParam("ids") List<Long> ids) {
        // Gọi phương thức trong service để thực hiện tìm kiếm theo danh sách ID.
        List<DanhGiaEntity> resultList = service.findByIds(ids);
        System.out.println("resultList: " + resultList);
        if (resultList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resultList);
        }
    }

//    @DeleteMapping("/xoa/{id}")
//    public void xoaDanhGia(@PathVariable Long id){
//        service.xoaDanhGia(id);
//    }

    @PatchMapping("/an/{id}")
    public void anDanhGia(@PathVariable Long id){
        service.anDanhGia(id);
    }
    @GetMapping("public/lay-danh-gia/{id}")
    public Page<DanhGiaDto> layDanhGiaChoSp(@PathVariable Long id, @RequestParam(required = false) String q, Pageable pageable){
        return service.layDanhGiaChoSp(id, q, pageable);
    }

}
