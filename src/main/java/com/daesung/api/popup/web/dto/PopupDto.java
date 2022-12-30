package com.daesung.api.popup.web.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class PopupDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "시작일자는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료일자는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "노출순서는 필수입니다.")
    private String sequence;

    private String fileSummary;
//    private String regUser;

}
