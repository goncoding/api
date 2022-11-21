package com.daesung.api.contact.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ContactUsUpdateDto {

    private String cuName;
    @Email(message = "이메일 형식을 확인 해주세요.")
    private String cuEmail;
    private String cuPhone;
    private String cuContent;
    private String cuCheck;
    private String cuAnswer;
    private String mnNum;
    private String cuMemo;
    private String busFieldName;


}
