package com.daesung.api.history.resource;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryRecord;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class HistoryRecordFileResource  extends EntityModel<HistoryRecord> {

    public HistoryRecordFileResource(HistoryRecord content, Link... links) {
        super(content, links);
    }
}
