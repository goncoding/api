package com.daesung.api.history.web.dto;

import com.daesung.api.history.domain.History;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryUpdateResponse {

    private History history;

    private Integer width;
    private Integer height;


}
