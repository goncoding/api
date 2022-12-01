package com.daesung.api.contact.web.dto;

import com.daesung.api.common.domain.BusinessField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactBusFieldDto {

    private Long busFieldId;
    private String busFieldNum;
    private String busFieldName;
    private String busFieldInfo;

    public ContactBusFieldDto(BusinessField businessField) {
        this.busFieldId = businessField.getId();
        this.busFieldNum = businessField.getBusFieldNum();
        this.busFieldName = businessField.getBusFieldName();
        this.busFieldInfo = businessField.getBusFieldInfo();
    }
}
