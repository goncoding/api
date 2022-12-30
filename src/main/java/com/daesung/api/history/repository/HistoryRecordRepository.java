package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Long>, HistoryRecordRepositoryCustom {

    Optional<HistoryRecord> findByIdAndLanguage(Long id, String lang);

    Page<HistoryRecord> searchRecordList(Search search, Pageable pageable);

    HistoryRecord searchPrevRecord(Long id, Search search);

    HistoryRecord searchNextRecord(Long id, Search search);

}
