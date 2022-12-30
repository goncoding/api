package com.daesung.api.contact.repository.condition;

import com.daesung.api.accounts.domain.enumType.AccountRole;
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

    private String searchType; //조회 조건

    private String searchName;
    private String searchText;
    private String searchFieldName;
    private String searchMnName;
    private AccountRole currentRole;


}
