package com.daesung.api.ir.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IrInfoRepositoryCustom {

    Page<IrInfo> searchIrInfoList(Search search, Pageable pageable);
}
