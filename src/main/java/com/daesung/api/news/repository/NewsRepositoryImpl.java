package com.daesung.api.news.repository;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.condition.NewsSearchCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.util.List;

import static com.daesung.api.news.domain.QNews.news;

@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepositoryCustom{

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<News> searchNewsList(NewsSearchCondition condition, Pageable pageable) {

        QueryResults<News> results = queryFactory
                .select(news)
                .from(news)
                .where(
                        titleEq(condition.getSearchTitle()),
                        textEq(condition.getSearchText()),
                        TypeEq(condition.getNbType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(NewsSort(pageable))
                .fetchResults();

        List<News> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * OrderSpecifier 를 쿼리로 반환하여 정렬조건을 맞춰준다.
     * 리스트 정렬
     * @param page
     * @return
     */
    private OrderSpecifier<?> NewsSort(Pageable page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            Sort sort = page.getSort();
            for (Sort.Order order : sort) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                String property = order.getProperty();
                switch (property) {
                    case "newsId":
                        return new OrderSpecifier<>(direction,news.id); //최신, 오래된 순
                    case "regDate":
                        return new OrderSpecifier<>(direction,news.regDate); //조회수순
                    case "updDate":
                        return new OrderSpecifier<>(direction,news.updDate); //이름순
                }
            }
        }
        return new OrderSpecifier<>(Order.DESC,news.id);
    }

    private BooleanExpression titleEq(String searchTitle) {
        return searchTitle == null ? null : news.title.contains(searchTitle);
    }

    private BooleanExpression textEq(String searchText) {
        return searchText == null ? null : news.content.contains(searchText);
    }

    private BooleanExpression TypeEq(NbType nbType) {
        return nbType == null ? null : news.nbType.eq(nbType);
    }


}
