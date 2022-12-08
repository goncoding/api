package com.daesung.api.news.web.dto;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.NewsThumbnailFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsGetResponse {

    private News news;
    private NewsThumbnailFile newsThumbnailFile;
    private List<NewsFile> newsImgList;

    private News prevNews;
    private News nextNews;


}
