package com.daesung.api.news.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Schema(description = "new 리스트 응답DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    @Schema(description = "뉴스(NE), 보도(RE)", example = "NE", allowableValues = {"NE","RE"})
    @NotEmpty(message = "뉴스(NE), 보도(RE)는 필수입니다.")
    private String nbType; //NE("뉴스"), RE("보도");
    @Schema(description = "뉴스/보도 제목", example = "대성, 창립 75주년 기념식 개최", required = true)
    private String title;
    @Schema(description = "뉴스 내용", example = "대성은 10일 오전, 대성산업 및 계열사 임직원 150여명이 참석한 가운데 대성디큐브아트센터 대극장에서 창립 75주년 기념식을 가졌습니다.", required = true)
    private String content;
    @Schema(description = "보도 회사", example = "투데이 에너지", required = true)
    private String newCompany;
    @Schema(description = "보도 link", example = "http://www.todayenergy.kr/news/articleView.html?idxno=246394", required = true)
    private String link;
    private Long viewCnt;

    @Schema(description = "언어", example = "kr", allowableValues = {"kr","en"})
    private String language;



}
