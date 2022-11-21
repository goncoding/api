package com.daesung.api.ethical.resource;

import com.daesung.api.ethical.domain.EthicalReport;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EthicalReportResource extends EntityModel<EthicalReport> {

    public EthicalReportResource(EthicalReport content, Link... links) {
        super(content, links);
//        add(linkTo())
    }
}
