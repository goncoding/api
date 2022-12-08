package com.daesung.api.history.resource;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.web.HistoryRecordController;
import com.daesung.api.history.web.dto.HistoryRecordInsertResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HistoryRecordInsertResource extends EntityModel<HistoryRecordInsertResponse> {

    public HistoryRecordInsertResource(HistoryRecordInsertResponse content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(HistoryRecordController.class).recordUpdate(content.getHistoryRecord().getId(), null,null,null,null,null,null,null,content.getHistoryRecord().getLanguage())).withRel("update-historyRecord"));
    }
}
