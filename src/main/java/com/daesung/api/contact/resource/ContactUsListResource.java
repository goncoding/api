package com.daesung.api.contact.resource;

import com.daesung.api.common.web.ManagerController;
import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.web.ContactUsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ContactUsListResource extends EntityModel<ContactUs> {

    public ContactUsListResource(ContactUs contactUs, Link... links) {
        super(contactUs, links);
        add(linkTo(methodOn(ContactUsController.class).contactGet(contactUs.getCuId(), contactUs.getLanguage())).withSelfRel());
        add(linkTo(methodOn(ManagerController.class).getManager(contactUs.getManager().getMnNum(), contactUs.getLanguage())).withRel("get-manager"));
    }
}
