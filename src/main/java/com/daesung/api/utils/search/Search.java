package com.daesung.api.utils.search;


import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.utils.StrUtil;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    private Long id;

    private String searchType;
    private String searchTitle;
    private String searchText;

    private Integer size;
    private Integer page;

    private String language;

    private String nbType;

    /**
     * 연혁 히스테리 기록 : NA("신년사"), CS("기념사"), CI("CI");
     */
    private HrCategory hrCategory;



    /**
     * IR 자료관리 :  MP("경영실적"), AR("감사보고서"),BR("사업보고서");
     */
    private IrType irType;



}
