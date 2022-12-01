package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {

    News searchPrevNews(Long id, NewsSearchCondition condition);

    News searchNextNews(Long id, NewsSearchCondition condition);



}
