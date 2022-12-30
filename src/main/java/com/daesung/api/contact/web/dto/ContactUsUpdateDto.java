package com.daesung.api.contact.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ContactUsUpdateDto {


    @NotBlank(message = "성함은 필수입니다.")
    private String cuName;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 확인 해주세요.")
    private String cuEmail;
    @NotBlank(message = "연락처는 필수입니다.")
    private String cuPhone;
    @NotBlank(message = "문의내용은 필수입니다.")
    private String cuContent;
    @NotBlank(message = "확인 여부는 필수입니다.")
    private String cuCheck;
    private String mnNum;
    @NotBlank(message = "담당자명은 필수입니다.")
    private String mnName;
    @NotBlank(message = "답변 내용은 필수입니다.")
    private String cuAnswer;
    private String cuMemo;
    private String busFieldName;


}
