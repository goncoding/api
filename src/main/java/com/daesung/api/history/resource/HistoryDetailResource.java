package com.daesung.api.history.resource;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import com.daesung.api.history.web.HistoryController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HistoryDetailResource extends EntityModel<HistoryDetail> {

    public HistoryDetailResource(HistoryDetail detail, Link... links) {
        super(detail, links);
        add(linkTo(methodOn(HistoryController.class).historyDetailGet(detail.getId(),detail.getLanguage())).withSelfRel());
    }
}
