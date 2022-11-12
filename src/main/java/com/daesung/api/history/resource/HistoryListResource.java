package com.daesung.api.history.resource;

import com.daesung.api.history.domain.History;
import com.daesung.api.history.web.HistoryController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HistoryListResource extends EntityModel<History> {

    public HistoryListResource(History history, Link... links) {
        super(history, links);
        add(linkTo(methodOn(HistoryController.class).historyGet(history.getId(), history.getLanguage())).withSelfRel().withRel("history-get"));
        add(linkTo(methodOn(HistoryController.class).historyUpdate(history.getId(), null,null,null,null, history.getLanguage())).withSelfRel().withRel("history-update"));
        add(linkTo(methodOn(HistoryController.class).historyManagementGet(history.getId(), history.getLanguage())).withSelfRel().withRel("detail-management"));

    }
}
