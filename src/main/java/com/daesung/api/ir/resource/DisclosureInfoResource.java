package com.daesung.api.ir.resource;

import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.ir.web.DisclosureInfoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class DisclosureInfoResource extends EntityModel<DisclosureInfo> {

    public DisclosureInfoResource(DisclosureInfo content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(DisclosureInfoController.class).disclosureInfoDelete(content.getId(), content.getLanguage())).withRel("delete-disclosure-info"));
    }
}
