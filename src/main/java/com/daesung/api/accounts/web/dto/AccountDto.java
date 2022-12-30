package com.daesung.api.accounts.web.dto;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.utils.date.RegTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends RegTimeEntity{

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;
    @NotBlank(message = "패스워드는 필수입니다.")
    private String loginPwd;
//    @NotBlank(message = "사원번호는 필수입니다.")
//    private String acNum; //사원번호

    private String acName; //사원명

    @NotBlank(message = "대표 메일은 필수입니다.")
    private String acEmail; //사원명

    @NotNull(message = "권한은 필수입니다.")
    private Set<AccountRole> roles; //사원권한





}
