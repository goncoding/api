package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryRecordRepositoryCustom {

    Page<HistoryRecord> searchRecordList(Search search, Pageable pageable);

}
