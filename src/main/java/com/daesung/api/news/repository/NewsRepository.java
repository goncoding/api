package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {



}
