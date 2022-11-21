package com.daesung.api.ethical.repository;

import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.daesung.api.ethical.domain.QEthicalReport.*;

@RequiredArgsConstructor
public class EthicalReportRepositoryImpl implements EthicalReportRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<EthicalReport> searchEthicalList(EthicalSearchCondition condition, Pageable pageable) {

        QueryResults<EthicalReport> results = queryFactory
                .selectFrom(ethicalReport)
                .where(
                        nameEq(condition.getSearchName()),
                        textEq(condition.getSearchText()),
                        mnNameEq(condition.getSearchMnName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ethicalReport.id.desc())
                .fetchResults();

        List<EthicalReport> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

//    private Object EthicalSort(Pageable pageable) {
//        return null;
//    }

    private Predicate nameEq(String searchName) {
        return searchName == null ? null : ethicalReport.erName.contains(searchName);
    }

    private Predicate textEq(String searchText) {
        return searchText == null ? null : ethicalReport.erContent.contains(searchText);
    }

    private Predicate mnNameEq(String searchMnName) {
        return searchMnName == null ? null : ethicalReport.manager.mnName.contains(searchMnName);
    }


}
