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
public class UserMailDto {

    @NotBlank(message = "수신 메일 정보는 필수입니다.")
    private String receiver ;
    @NotBlank(message = "메일 제목은 필수입니다.")
    private String subject;
    @NotBlank(message = "메일 내용은 필수입니다.")
    private String content;

}

