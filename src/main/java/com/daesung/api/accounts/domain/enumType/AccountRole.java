package com.daesung.api.accounts.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AccountRole {

    //부서
    USER("익명인"),
    ADMIN("관리자"),
    INFO("정보시스템사업부"),
    PERSON("인사총무부"),
    FINANCE("재무IR기획부"),
    AUDIT("감사실"),

    //계열사 (1대1 문의 관련)
    DS_HEAD ("대성본사"),
    SEOUL_OIL ("서울석유부"),
    DAEGU_OIL ("대구석유부"),
    MACHINERY ("기계사업부"),
    OVERSEAS_RESOURCE ("해외자원개발부"),
    DS_CELTICS  ("대성쎌틱에너시스"),
    DS_HEAT ("대성히트에너시스"),
    DS_METERS ("대성계전"),
    KR_CAMBRIDGE ("한국캠브리지필터"),
    DS_HYDRAULIC  ("대성나찌유압공업"),
    DS_LOGISTICS ("대성물류건설"),
    D_CUBE_GEOJE ("디큐브거제백화점"),
    DS_CS  ("대성C&S"),
    DS_POWER ("DS파워"),
    KR_LOGISTICS ("한국물류용역"),
    DS_ART ("대성아트센터"),
    ETHICAL("윤리경영")
    ;

    private String description;

}
