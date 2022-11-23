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
    Long id;
    String searchType;
    String searchText;
    String nbType;

    Integer size;
    Integer page;

    private String searchTitle;


    /**
     * 연혁 히스테리 기록 : NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
     */
    private HrCategory hrCategory;

    /**
     * IR 자료관리 :  MP("경영실적"), AR("감사보고서"),BR("사업보고서");
     */
    private IrType irType;

    private String recordType;

//    public String getParams() {
//        final String[] params = {searchType, searchText, nbType, recordType};
//        String result = "";
//        for (int i = 0, size = params.length; i < size; i++) {
//            result += StrUtil.isEmpty(params[i]) ? "" : String.format("&param%d=%s", i + 1, params[i]);
//        }
//        return StrUtil.encodeKR(result.replaceAll("^&", ""));
//    }
//
//    public String getQuery() {
//        return String.format("%s&%s", getPageInfo(), getParams()).replaceAll("(^&|&$)", "");
//    }
//
//    public String getPageInfo() {
//        return String.format("page=%d&pageSize=%d", page, size);
//    }
}
