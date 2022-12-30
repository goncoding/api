package com.daesung.api.history.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorytUpdateDto {

    @NotEmpty(message = "연혁 내용은 필수입니다.")
    private String content;

}
