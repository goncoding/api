package com.daesung.api.history.repository;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.utils.search.Search;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.util.List;

import static com.daesung.api.history.domain.QHistoryRecord.*;

@RequiredArgsConstructor
public class HistoryRecordRepositoryImpl implements HistoryRecordRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HistoryRecord> searchRecordList(Search search, Pageable pageable) {

        QueryResults<HistoryRecord> results = queryFactory
                .selectFrom(historyRecord)
                .where(
                        titleEq(search.getSearchTitle()),
                        hrCategoryEq(search.getHrCategory()),
                        languageEq(search.getLanguage())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(historyRecord.id.desc())
                .fetchResults();

        List<HistoryRecord> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public HistoryRecord searchPrevRecord(Long id, Search search) {

        HistoryRecord prevRecord = queryFactory
                .selectFrom(historyRecord)
                .where(
                        titleEq(search.getSearchTitle()),
                        hrCategoryEq(search.getHrCategory()),
                        historyRecord.id.gt(id),
                        languageEq(search.getLanguage())
                )
                .limit(1)
                .orderBy(historyRecord.id.asc())
                .fetchOne();

        return prevRecord;
    }

    @Override
    public HistoryRecord searchNextRecord(Long id, Search search) {

        HistoryRecord nextRecord = queryFactory
                .selectFrom(historyRecord)
                .where(
                        titleEq(search.getSearchTitle()),
                        hrCategoryEq(search.getHrCategory()),
                        historyRecord.id.lt(id),
                        languageEq(search.getLanguage())
                )
                .limit(1)
                .orderBy(historyRecord.id.desc())
                .fetchOne();

        return nextRecord;
    }

    private BooleanExpression languageEq(String language) {
        return language == null ? null : historyRecord.language.contains(language);
    }

    private BooleanExpression titleEq(String searchTitle) {
        return searchTitle == null ? null : historyRecord.hrTitle.contains(searchTitle);
    }

    private BooleanExpression hrCategoryEq(HrCategory hrCategory) {
        return hrCategory == null ? null : historyRecord.hrCategory.eq(hrCategory);
    }
}
