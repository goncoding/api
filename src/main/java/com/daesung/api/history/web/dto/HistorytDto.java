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
public class HistorytDto {

    @NotEmpty(message = "연혁 제목은 필수입니다.")
    private String title;
    @NotEmpty(message = "연혁 내용은 필수입니다.")
    private String content;
    @NotEmpty(message = "시작 연도는 필수입니다.")
    private String hiStartYear;
    @NotEmpty(message = "종료 연도는 필수입니다.")
    private String hiEndYear;

    private String language;

}
