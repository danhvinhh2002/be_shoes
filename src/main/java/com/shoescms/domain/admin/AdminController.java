package com.shoescms.domain.admin;

import com.shoescms.common.model.response.BaseResponse;
import com.shoescms.common.model.response.CommonBaseResult;
import com.shoescms.common.model.response.CommonResult;
import com.shoescms.domain.admin.dto.RoleResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "09. Admin")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin")
public class AdminController {
    private final BaseResponse baseResponse;
    private final AdminService adminService;

    @Operation(summary = "권한 조회", description = "권한 조회")
    @GetMapping(value = "/role")
    public CommonResult<List<RoleResDto>> getRoles() {
        return baseResponse.getContentResult(adminService.getRoles());
    }

}
