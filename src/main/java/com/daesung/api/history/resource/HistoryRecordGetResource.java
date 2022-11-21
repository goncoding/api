package com.daesung.api.history.resource;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.web.dto.recordResponseDto;
import com.daesung.api.utils.search.SearchDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class HistoryRecordGetResource  extends EntityModel<recordResponseDto> {

    public HistoryRecordGetResource(recordResponseDto content, Link... links) {
        super(content, links);
    }


}
