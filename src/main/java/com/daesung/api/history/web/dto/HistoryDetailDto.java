package com.daesung.api.history.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDetailDto {

    @NotBlank(message = "기간은 필수입니다.")
    private String hdYear;
    @NotBlank(message = "월은 필수입니다.")
    private String hdMonth;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @NotNull(message = "노출 순서는 필수입니다.")
    private Integer hdSequence;

}
