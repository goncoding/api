package com.daesung.api.accounts.web.dto;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.domain.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    private String loginId;
    private String acName; //사원명
    private String acEmail; //공용이메일]

    private AccountRole accountRole;

    public AccountResponseDto(Account account) {
        this.loginId = account.getLoginId();
        this.acName = account.getAcName();
        this.acEmail = account.getAcEmail();
    }
}
