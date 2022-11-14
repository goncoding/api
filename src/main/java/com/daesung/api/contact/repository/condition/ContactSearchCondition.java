package com.daesung.api.contact.repository.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactSearchCondition {

    private Long searchId;
    private String searchName;
    private String searchText;
    private String searchFieldName;
    private String searchMnName;


}
