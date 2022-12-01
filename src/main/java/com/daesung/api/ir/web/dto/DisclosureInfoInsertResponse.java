package com.daesung.api.ir.web.dto;

import com.daesung.api.history.domain.History;
import com.daesung.api.ir.domain.DisclosureInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisclosureInfoInsertResponse {

    private DisclosureInfo disclosureInfo;

    private Integer width;
    private Integer height;

}
