package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import com.daesung.api.history.web.dto.HistoryDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HistoryDetailRepository extends JpaRepository<HistoryDetail, Long> {

    HistoryDetail findByHdSequence(Integer sequence);

    Optional<HistoryDetail> findByIdAndLanguage(Long id, String lang);

    Page<HistoryDetail> findByLanguage(String lang, Pageable pageable);

    HistoryDetail findByHdYearAndHdMonthAndHdSequence(String year, String month, Integer sequence);

    List<HistoryDetail> findByHdYearAndHdMonthOrderByHdSequence(String year, String month);

    @Query("select hd from HistoryDetail hd where hd.history.id = :historyId")
    List<HistoryDetail> findByHistoryId(Long historyId);

    @Query("select hd from HistoryDetail hd " +
            "where hd.hdYear = :year and hd.hdMonth = :month " +
            "and hd.hdSequence >= :sequence")
    List<HistoryDetail> findByHdSequenceEqYearPlus(String year, String month, Integer sequence);

    @Query("select hd from HistoryDetail hd " +
            "where hd.hdYear = :year and hd.hdMonth = :month " +
            "and hd.hdSequence > :sequence")
    List<HistoryDetail> findByHdSequenceEqYearMinus(String year, String month, Integer sequence);

    //입력값 > 기존값
    @Query("select hd from HistoryDetail hd " +
            "where hd.hdYear = :year and hd.hdMonth = :month " +
            "and hd.hdSequence >= :existSeq and hd.hdSequence <= :inputSeq")
    List<HistoryDetail> findByHdSequenceLtInputSeq(String year, String month, Integer inputSeq, Integer existSeq);

    //기존값 > 입력값
    @Query("select hd from HistoryDetail hd " +
            "where hd.hdYear = :year and hd.hdMonth = :month " +
            "and hd.hdSequence >= :inputSeq and hd.hdSequence <= :existSeq")
    List<HistoryDetail> findByHdSequenceGtInputSeq(String year, String month, Integer inputSeq, Integer existSeq);











}
