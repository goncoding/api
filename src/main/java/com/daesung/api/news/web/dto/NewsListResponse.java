package com.daesung.api.news.web.dto;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsThumbnailFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsListResponse {

    private News news;
    private NewsThumbnailFile newsThumbnailFile;

}
