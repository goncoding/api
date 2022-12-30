package com.daesung.api.ethical.web.dto;

import com.daesung.api.accounts.web.dto.AccountResponseDto;
import com.daesung.api.ethical.domain.EthicalReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EthicalReportGetResponse {

    private EthicalReport ethicalReport;
    private AccountResponseDto accountResponseDto;

}
