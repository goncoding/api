package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DisclosureInfoRepository extends JpaRepository<DisclosureInfo, Long> , DisclosureInfoRepositoryCustom{

    Page<DisclosureInfo> searchDisclosureInfoList(Search search, Pageable pageable);

}
