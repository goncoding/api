package com.daesung.api.utils.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    private String searchType;
    private String searchText;
    private String recordType;

}
