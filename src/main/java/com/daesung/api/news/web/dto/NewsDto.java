package com.daesung.api.news.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    @NotEmpty(message = "뉴스(NE), 보도(RE)는 필수입니다.")
    private String nbType; //NE("뉴스"), RE("보도");

    private String title;
    private String content;
    private String newCompany;
    private String link;
    private Long viewCnt;
    private String language;
    private String thumbSummary;

    @NotNull(message = "등록일자는 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate selectRegDate;



}
