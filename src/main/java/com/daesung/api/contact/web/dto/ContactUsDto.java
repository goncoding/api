package com.daesung.api.contact.web.dto;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class ContactUsDto {

    @NotBlank(message = "성함은 필수입니다.")
    private String cuName;
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 확인 해주세요.")
    private String cuEmail;
    @NotBlank(message = "연락처는 필수입니다.")
    private String cuPhone;
    @NotBlank(message = "문의내용은 필수입니다.")
    private String cuContent;
    private String cuCheck;
    private String cuAnswer;
    private String mnNum;
    private String cuMemo;

}
