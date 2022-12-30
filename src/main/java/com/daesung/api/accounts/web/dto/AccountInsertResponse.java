package com.daesung.api.accounts.web.dto;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInsertResponse {

    private Long id;
    private String loginId;
    private String acName; //사원명
    private String acEmail;
    private Set<AccountRole> roles; //사원권한
    private String regDate;

}
