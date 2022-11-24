package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.utils.search.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.daesung.api.ir.domain.QDisclosureInfo.*;


@RequiredArgsConstructor
public class DisclosureInfoRepositoryImpl implements DisclosureInfoRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DisclosureInfo> searchDisclosureInfoList(Search search, Pageable pageable) {

        QueryResults<DisclosureInfo> results = queryFactory
                .selectFrom(disclosureInfo)
                .where(
                        titleEq(search.getSearchTitle())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(disclosureInfo.id.desc())
                .fetchResults();

        List<DisclosureInfo> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleEq(String searchTitle) {
        return searchTitle == null ? null : disclosureInfo.diTitle.contains(searchTitle);
    }
}
