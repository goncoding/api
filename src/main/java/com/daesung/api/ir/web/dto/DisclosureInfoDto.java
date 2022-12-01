package com.daesung.api.ir.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class DisclosureInfoDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String diTitle;

    private String regUser;




}
