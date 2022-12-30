package com.daesung.api.ethical.repository.condition;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EthicalSearchCondition {

    private Long searchId;

    private String searchType; //조회 조건

    private String searchName;
    private String searchText;
    private String searchMnName;

    private String page;
    private String size;
    private AccountRole currentRole;

}
