package com.daesung.api.ethical.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EthicalReportUpdateDto {


    private String erName;

    @Email(message = "이메일 형식을 확인 해주세요.")
    private String erEmail;
    private String erPhone;
    private String erTitle;
    private String erContent;
    private String erCheck;
    private String erAnswer;
    private String mnNum;
    private String erMemo;
    private String busFieldName;


}
