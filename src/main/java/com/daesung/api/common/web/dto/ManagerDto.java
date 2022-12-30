package com.daesung.api.common.web.dto;

import com.daesung.api.common.domain.BusinessField;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ManagerDto {

//    @NotBlank(message = "사번은 필수입니다.")
    private String mnNum;
    @NotBlank(message = "이름은 필수입니다.")
    private String mnName;
//    @NotBlank(message = "부서는 필수입니다.")
    private String mnDeptNum;
//    @NotBlank(message = "사업분야 번호는 필수입니다.")
    private String busFieldNum;
//    @NotBlank(message = "직급은 필수입니다.")
    private String mnPosition;
//    @NotBlank(message = "연락처는 필수입니다.")
    private String mnPhone;
//    @NotBlank(message = "이메일은 필수입니다.")
    private String mnEmail;
}










