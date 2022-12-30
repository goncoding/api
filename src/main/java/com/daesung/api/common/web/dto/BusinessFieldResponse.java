package com.daesung.api.common.web.dto;

import com.daesung.api.common.domain.BusinessField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessFieldResponse {

    private BusinessField businessField;

//    private BusinessDto businessDto;

}
