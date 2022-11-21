package com.daesung.api.ethical.web;

import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthicalReportListResponse {

    Page<EthicalReport> pagedModel;

    EthicalSearchCondition searchCondition;


}
