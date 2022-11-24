package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IrInfoRepository extends JpaRepository<IrInfo, Long>, IrInfoRepositoryCustom{

    Page<IrInfo> searchIrInfoList(Search search, Pageable pageable);

}
