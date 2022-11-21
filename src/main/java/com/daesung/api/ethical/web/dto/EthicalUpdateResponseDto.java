package com.daesung.api.ethical.web.dto;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.domain.enumType.ErCheck;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EthicalUpdateResponseDto {

   private EthicalReport ethicalReport;

   private Manager manager;

}
