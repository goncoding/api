package com.daesung.api.popup.repository;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.popup.domain.Popup;
import com.daesung.api.popup.domain.QPopup;
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

import static com.daesung.api.ir.domain.QDisclosureInfo.disclosureInfo;
import static com.daesung.api.popup.domain.QPopup.*;

@RequiredArgsConstructor
public class PopupRepositoryImpl implements PopupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Popup> searchPopupList(Search search, Pageable pageable) {

        QueryResults<Popup> results = queryFactory
                .selectFrom(popup)
                .where(
                        titleEq(search.getSearchTitle())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(popup.puStartDate.desc(), popup.puSequence.asc())
                .fetchResults();

        List<Popup> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleEq(String searchTitle) {
        return searchTitle == null ? null : popup.puTitle.contains(searchTitle);
    }

}
