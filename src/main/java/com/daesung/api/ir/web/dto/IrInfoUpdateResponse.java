package com.daesung.api.ir.web.dto;

import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.domain.IrYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrInfoUpdateResponse {


    private IrInfo irInfo;
    private String irYear;

    private Integer width;
    private Integer height;


}
