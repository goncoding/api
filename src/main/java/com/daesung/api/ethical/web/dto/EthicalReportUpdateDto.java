package com.daesung.api.ethical.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EthicalReportUpdateDto {

    @NotBlank(message = "성함은 필수입니다.")
    private String erName;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 확인 해주세요.")
    private String erEmail;
    @NotBlank(message = "연락처는 필수입니다.")
    private String erPhone;
    @NotBlank(message = "문의제목은 필수입니다.")
    private String erTitle;
    @NotBlank(message = "문의내용은 필수입니다.")
    private String erContent;
    @NotBlank(message = "확인 여부는 필수입니다.")
    private String erCheck;
    @NotBlank(message = "답변 내용은 필수입니다.")
    private String erAnswer;
    private String mnNum;
    @NotBlank(message = "담당자명은 필수입니다.")
    private String mnName;
    private String erMemo;
    private String busFieldName;


}
