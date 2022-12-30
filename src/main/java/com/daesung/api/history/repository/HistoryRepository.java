package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface HistoryRepository extends JpaRepository<History, Long>{

    @Query("select h " +
            "from History h " +
            "where :detailYear between h.hiStartYear and h.hiEndYear " +
            "and h.language = :lang")
    History searchHistoryBetween(String detailYear, String lang);

    Optional<History> findByIdAndLanguage(Long id, String lang);

    Page<History> findByLanguage(String lang, Pageable pageable);




}
