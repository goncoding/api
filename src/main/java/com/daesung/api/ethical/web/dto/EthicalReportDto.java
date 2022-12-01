package com.daesung.api.ethical.web.dto;

import com.daesung.api.common.domain.enumType.ConsentStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class EthicalReportDto {

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
    @NotNull(message = "동의여부는 필수입니다.")
    private ConsentStatus consentStatus;

    private String erCheck;
    private String erAnswer;
    private String mnNum;
    private String erMemo;
    private String busFieldName;


}
