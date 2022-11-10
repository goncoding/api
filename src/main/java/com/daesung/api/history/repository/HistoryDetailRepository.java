package com.daesung.api.history.repository;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import com.daesung.api.history.web.dto.HistoryDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryDetailRepository extends JpaRepository<HistoryDetail, Long> {

    HistoryDetail findByHdSequence(Integer sequence);

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


}
