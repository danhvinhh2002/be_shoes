package com.shoescms.domain.product.controller;


import com.shoescms.domain.product.dto.ResponseDto;
import com.shoescms.domain.product.dto.ThuongHieuDTO;
import com.shoescms.domain.product.models.ThuongHieuModel;
import com.shoescms.domain.product.service.IThuongHieuService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "03.4. Thuong hieu")
@RestController
@RequestMapping("/v1/thuong-hieu")
public class ThuongHieuController {

    @Autowired
    IThuongHieuService iThuongHieuService;

    @GetMapping("loc-thuong-hieu")
    public Page<ThuongHieuDTO> locThuongHieu(@Parameter(name = "tenThuongHieu") @RequestParam(required = false) String tenThuongHieu,
                                             @Parameter(name = "layMacDinh") @RequestParam(defaultValue = "") String layMacDinh,
                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable){
        return iThuongHieuService.locThuongHieu(tenThuongHieu, layMacDinh, pageable);
    };
    @PostMapping("/add")
    public ResponseDto addThuongHieu(@RequestBody ThuongHieuModel thuongHieuModel){
        return ResponseDto.of(iThuongHieuService.add(thuongHieuModel));

    }

    @PutMapping("/update/{id}")
    public ResponseDto updateThuongHieu(@RequestBody ThuongHieuModel thuongHieuModel) {
        return ResponseDto.of(iThuongHieuService.update(thuongHieuModel));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseDto deleteThuongHieu(@PathVariable Long id) {
        return ResponseDto.of(iThuongHieuService.deleteById(id));
    }
}
