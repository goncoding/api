package com.daesung.api.common.resource;

import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.web.ManagerController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ManagerResource extends EntityModel<Manager> {

    public ManagerResource(Manager manager, Link... links) {
        super(manager, links);
        add(linkTo(methodOn(ManagerController.class).getManager(manager.getMnNum(), manager.getLanguage())).withSelfRel());
    }
}
