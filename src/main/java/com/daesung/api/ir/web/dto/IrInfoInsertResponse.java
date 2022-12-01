package com.daesung.api.ir.web.dto;

import com.daesung.api.ir.domain.IrInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IrInfoInsertResponse {

    private IrInfo irInfo;

    private Integer width;
    private Integer height;


}
