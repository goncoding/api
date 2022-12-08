package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IrInfoRepository extends JpaRepository<IrInfo, Long>, IrInfoRepositoryCustom{

    Page<IrInfo> searchIrInfoList(Search search, Pageable pageable);

    @Query("select i from IrInfo i where i.irYear.id = :id")
    List<IrInfo> findByIrYear(Long id);

    @Query("select i from IrInfo i order by i.id desc")
    List<IrInfo> findbyLastIrId(Pageable pageable);
}
