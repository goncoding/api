package com.daesung.api.ir.web.dto;

import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.domain.enumType.IrType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class IrInfoDto {

    @NotBlank(message = "연도는 필수입니다.")
    @Pattern(regexp = "\\d{4}", message = "숫자 4자리만 가능합니다.")
    private String irYear; // 등록연도
    @NotBlank(message = "IR 자료는 필수입니다.")
    private String irType; //Ir 자료 구문
    @NotBlank(message = "제목은 필수입니다.")
    private String irTitle;

    private String year;

}
