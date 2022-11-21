package com.daesung.api.ir.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IrType {

//    MANAGEMENT_PERFORMANCE("경영실적"), AUDIT_REPORT("감사보고서"),BUSINESS_REPORT("사업보고서");

    MP("경영실적"), AR("감사보고서"),BR("사업보고서");

    private String description;

}
