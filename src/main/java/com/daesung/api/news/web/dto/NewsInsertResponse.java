package com.daesung.api.news.web.dto;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.news.domain.News;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsInsertResponse {

    private News news;

    private Integer thumbWidth;
    private Integer thumbHeight;



}
