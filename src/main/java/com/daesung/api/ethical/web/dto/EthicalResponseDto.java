package com.daesung.api.ethical.web.dto;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.ethical.domain.EthicalReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthicalResponseDto {

    private EthicalReport ethicalReport;

    private BusinessField businessField;

}
