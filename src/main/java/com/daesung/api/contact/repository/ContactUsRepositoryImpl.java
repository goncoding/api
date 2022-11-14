package com.daesung.api.contact.repository;

import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.repository.condition.ContactSearchCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.daesung.api.contact.domain.QContactUs.*;

@RequiredArgsConstructor
public class ContactUsRepositoryImpl implements ContactUsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ContactUs> searchContactList(ContactSearchCondition condition, Pageable pageable) {

        QueryResults<ContactUs> results = queryFactory
                .selectFrom(contactUs)
                .where(
                        nameEq(condition.getSearchName()),
                        textEq(condition.getSearchText()),
                        busFieldNameEq(condition.getSearchFieldName()),
                        mnNameEq(condition.getSearchMnName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ContactSort(pageable))
                .fetchResults();

        List<ContactUs> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> ContactSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            Sort sort = page.getSort();
            for (Sort.Order order : sort) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                String property = order.getProperty();
                switch (property) {
                    case "cuId":
                        return new OrderSpecifier<>(direction,contactUs.cuId);
                    case "cuName":
                        return new OrderSpecifier<>(direction,contactUs.cuName);
                    case "regDate":
                        return new OrderSpecifier<>(direction,contactUs.regDate);
                }
            }
        }
        return new OrderSpecifier<>(Order.DESC,contactUs.cuId);
    }

    private Predicate nameEq(String searchName) {
        return searchName == null ? null : contactUs.cuName.contains(searchName);
    }

    private Predicate textEq(String searchText) {
        return searchText == null ? null : contactUs.cuContent.contains(searchText);
    }

    private Predicate busFieldNameEq(String searchFieldName) {
        return searchFieldName == null ? null : contactUs.businessField.busFieldName.contains(searchFieldName);
    }

    private Predicate mnNameEq(String searchName) {
        return searchName == null ? null : contactUs.manager.mnName.contains(searchName);
    }

}
