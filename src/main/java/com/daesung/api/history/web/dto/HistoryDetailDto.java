package com.daesung.api.history.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDetailDto {

    @NotBlank(message = "기간은 필수입니다.")
    @Pattern(regexp = "\\d{4}", message = "년도는 숫자 4자리만 가능합니다.")
    private String hdYear;
    @NotBlank(message = "월은 필수입니다.")
    @Pattern(regexp = "\\d{1,2}", message = "월은 숫자 2자리까지만 가능합니다.")
    private String hdMonth;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @NotNull(message = "노출 순서는 필수입니다.")
    private Integer hdSequence;

}
