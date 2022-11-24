package com.daesung.api.esg.resource;

import com.daesung.api.esg.domain.Esg;
import com.daesung.api.esg.web.EsgController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class esgResource extends EntityModel<Esg> {

    public esgResource(Esg content, Link... links) {
        super(content, links);
        add(linkTo(EsgController.class,content.getLanguage()).slash(content.getId()).withRel("update-esg-file"));
    }
}
