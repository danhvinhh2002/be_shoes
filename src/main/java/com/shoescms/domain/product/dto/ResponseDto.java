package com.shoescms.domain.product.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseDto {
    private String code = "code0";
    private Object data;
    private Object otherData;

    public static ResponseDto of(int code, Object data) {
        return ResponseDto.builder()
                .code("code" + code)
                .data(data)
                .build();
    }

    public static ResponseDto of(Object data) {
        return ResponseDto.builder()
                .data(data)
                .build();
    }

    public static ResponseDto otherData(Object data, Object otherData) {
        return ResponseDto.builder()
                .data(data)
                .otherData(otherData)
                .build();
    }


    public static ResponseDto ofError(int code) {
        return ResponseDto
                .builder()
                .code("code" + code)
                .build();
    }
}
