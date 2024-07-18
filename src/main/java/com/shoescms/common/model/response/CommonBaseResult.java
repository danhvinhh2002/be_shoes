package com.shoescms.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonBaseResult {
    @Schema(description = "응답 코드 번호 : >= 0 정상, < 0 비정상")
    private int code;

    @Schema(description = "응답 메시지")
    private String message;
}