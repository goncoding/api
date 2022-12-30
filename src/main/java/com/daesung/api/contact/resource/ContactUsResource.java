package com.daesung.api.contact.resource;

import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.web.ContactUsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ContactUsResource extends EntityModel<ContactUs> {

    public ContactUsResource(ContactUs contactUs, Link... links) {
        super(contactUs, links);
//        add(linkTo(methodOn(ContactUsController.class).contactGet(contactUs.getId(), contactUs.getLanguage(),null)).withSelfRel());
    }
}
