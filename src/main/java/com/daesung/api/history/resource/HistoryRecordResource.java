package com.daesung.api.history.resource;

import com.daesung.api.history.domain.HistoryRecord;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class HistoryRecordResource extends EntityModel<HistoryRecord> {

    public HistoryRecordResource(HistoryRecord content, Link... links) {
        super(content, links);

    }
}
