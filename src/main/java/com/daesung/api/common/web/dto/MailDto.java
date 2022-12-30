package com.daesung.api.common.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    @NotBlank(message = "발신인은 필수입니다.")
    private String senderName;
    @NotBlank(message = "발신 메일은 필수입니다.")
    private String senderMail;
    @NotBlank(message = "답신(문의자) 메일은 필수입니다.")
    private String userMail;
    @NotBlank(message = "제목은 필수입니다.")
    private String subject;
    @NotBlank(message = "내용은 필수입니다.")
    private String bodyText;


}
