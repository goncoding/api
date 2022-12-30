package com.daesung.api.common.resource;

import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.web.ManagerController;
import com.daesung.api.common.web.dto.ManagerDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ManagerDtoResource extends EntityModel<ManagerDto> {

    public ManagerDtoResource(ManagerDto managerDto, String lang, Link... links) {
        super(managerDto, links);
        add(linkTo(methodOn(ManagerController.class).managerGet(managerDto.getMnNum(), lang)).withSelfRel());
    }
}
