package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {

    Page<News> searchNewsList(NewsSearchCondition condition, Pageable pageable);

    News searchPrevNews(Long id, NewsSearchCondition condition);

    News searchNextNews(Long id, NewsSearchCondition condition);

}
