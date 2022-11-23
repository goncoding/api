package com.daesung.api.history.resource;

import com.daesung.api.history.domain.HistoryRecord;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class HistoryRecordUpdateResource extends EntityModel<HistoryRecord> {


    public HistoryRecordUpdateResource(HistoryRecord content, Link... links) {
        super(content, links);
    }
}
