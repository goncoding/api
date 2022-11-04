package com.daesung.api.news.web.dto;

import com.daesung.api.news.domain.enumType.NbType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsForm {

    private String nbType; //NE("뉴스"), RE("보도");
    private String title;
    private String newCompany;
    private String content;
    private String link;
    private String viewCnt;
    private String language;


}
