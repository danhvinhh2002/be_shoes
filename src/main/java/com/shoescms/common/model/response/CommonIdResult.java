package com.shoescms.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonIdResult{
    @Schema(description = "아이디", example = "1")
    private final Long id;
}
