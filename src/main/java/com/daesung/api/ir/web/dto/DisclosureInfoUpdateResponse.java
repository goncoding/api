package com.daesung.api.ir.web.dto;

import com.daesung.api.ir.domain.DisclosureInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisclosureInfoUpdateResponse {

    private DisclosureInfo disclosureInfo;

    private Integer width;
    private Integer height;


}
