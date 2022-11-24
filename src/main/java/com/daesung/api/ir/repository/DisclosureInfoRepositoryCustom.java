package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DisclosureInfoRepositoryCustom {

    Page<DisclosureInfo> searchDisclosureInfoList(Search search, Pageable pageable);
}
