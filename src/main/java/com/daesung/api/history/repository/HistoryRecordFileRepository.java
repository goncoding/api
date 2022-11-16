package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRecordFileRepository extends JpaRepository<HistoryRecordFile, Long> {

    @Query("select hf " +
            "from HistoryRecordFile hf " +
            "where hf.historyRecord.id = :id " +
            "order by hf.hrFileSeq asc")
    List<HistoryRecordFile> findByHrId(Long id);

    @Query("select hf " +
            "from HistoryRecordFile hf " +
            "where hf.historyRecord.id = :id and hf.hrFileSeq = :seq")
    HistoryRecordFile findByHrIdAndSeq(Long id, String seq);
}
