package com.daesung.api.common.web.dto;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponse {

    private Manager manager;

    private BusinessField businessField;


}
