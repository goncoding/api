package com.daesung.api.news.web.dto;

import com.daesung.api.common.response.CommonResponse;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.NewsThumbnailFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsGetResponseDto{

    private News news;
    private NewsThumbnailFile newsThumbnailFile;
    private List<NewsFile> newsImgList;


}
