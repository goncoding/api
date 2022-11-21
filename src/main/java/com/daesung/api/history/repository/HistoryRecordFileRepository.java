package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HistoryRecordFileRepository extends JpaRepository<HistoryRecordFile, Long> {

    @Query("select hf " +
            "from HistoryRecordFile hf " +
            "where hf.historyRecord.id = :id " +
            "order by hf.hrFileSeq asc")
    List<HistoryRecordFile> findByHrId(Long id);

    @Query("select hf " +
            "from HistoryRecordFile hf " +
            "where hf.historyRecord.id = :id and hf.hrFileSeq = :seq")
    Optional<HistoryRecordFile> findByHrIdAndSeq(Long id, String seq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete " +
            "from HistoryRecordFile hf " +
            "where hf.historyRecord.id = :id")
    void deleteByHrId(Long id);
}
