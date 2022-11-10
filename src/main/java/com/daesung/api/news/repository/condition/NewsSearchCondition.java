package com.daesung.api.news.repository.condition;

import com.daesung.api.news.domain.enumType.NbType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsSearchCondition {

    private Long searchId;
    private String searchTitle;
    private String searchText;
    private NbType nbType; // 뉴스, 보도


}
