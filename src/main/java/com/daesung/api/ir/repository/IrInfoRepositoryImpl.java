package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.utils.search.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.daesung.api.ir.domain.QIrInfo.*;
import static com.daesung.api.ir.domain.QIrYear.*;

@RequiredArgsConstructor
public class IrInfoRepositoryImpl implements IrInfoRepositoryCustom{

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<IrInfo> searchIrInfoList(Search search, Pageable pageable) {

        QueryResults<IrInfo> results = queryFactory
                .select(irInfo)
                .from(irInfo)
                .leftJoin(irInfo.irYear, irYear)
                .fetchJoin()
                .where(
                        titleEq(search.getSearchTitle()),
                        irTypeEq(search.getIrType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(irYear.iyYear.desc(), irInfo.regDate.desc())
                .orderBy(irInfo.id.desc())
                .fetchResults();

        List<IrInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleEq(String searchTitle) {
        return searchTitle == null ? null : irInfo.irTitle.contains(searchTitle);
    }

    private BooleanExpression irTypeEq(IrType irType) {
        return irType == null ? null : irInfo.irType.eq(irType);
    }
}
