package com.daesung.api.ir.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IrYearUpdateDto {

    @NotBlank(message = "연도는 필수입니다.")
    @Pattern(regexp = "\\d{4}", message = "숫자 4자리만 가능합니다.")
    private String iyYear;
}
