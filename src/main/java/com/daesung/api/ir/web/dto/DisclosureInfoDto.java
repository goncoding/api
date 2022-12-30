package com.daesung.api.ir.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisclosureInfoDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String diTitle;

    private String regUser;




}
