package com.daesung.api.utils.search;


import com.daesung.api.utils.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Search {
    Long id;
    String searchType;
    String searchText;
    String nbType;

    Integer size;
    Integer page;

    public String getParams() {
        final String[] params = {searchType, searchText, nbType};
        String result = "";
        for (int i = 0, size = params.length; i < size; i++) {
            result += StrUtil.isEmpty(params[i]) ? "" : String.format("&param%d=%s", i + 1, params[i]);
        }
        return StrUtil.encodeKR(result.replaceAll("^&", ""));
    }

    public String getQuery() {
        return String.format("%s&%s", getPageInfo(), getParams()).replaceAll("(^&|&$)", "");
    }

    public String getPageInfo() {
        return String.format("page=%d&pageSize=%d", page, size);
    }
}
