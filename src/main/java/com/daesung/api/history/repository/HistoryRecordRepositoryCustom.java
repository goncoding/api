package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.news.domain.News;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryRecordRepositoryCustom {

    Page<HistoryRecord> searchRecordList(Search search, Pageable pageable);

    HistoryRecord searchPrevRecord(Long id, Search search);

    HistoryRecord searchNextRecord(Long id, Search search);


}
