package com.daesung.api.ethical.repository;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.daesung.api.contact.domain.QContactUs.contactUs;
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

    private BooleanExpression nameEq(String searchName) {
        return searchName == null ? null : ethicalReport.erName.contains(searchName);
    }

    private BooleanExpression textEq(String searchText) {
        return searchText == null ? null : ethicalReport.erContent.contains(searchText);
    }

    private BooleanExpression mnNameEq(String searchMnName) {
        return searchMnName == null ? null : ethicalReport.mnName.contains(searchMnName);
    }

}
