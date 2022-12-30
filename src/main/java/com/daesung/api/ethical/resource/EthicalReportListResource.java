package com.daesung.api.ethical.resource;

import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.daesung.api.ethical.web.EthicalReportController;
import com.daesung.api.ethical.web.EthicalReportListResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter @Setter
public class EthicalReportListResource extends EntityModel<EthicalReport> {

//    private EthicalSearchCondition ethicalSearchCondition;

    public EthicalReportListResource(EthicalReport content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(EthicalReportController.class).ethicalGet(content.getId(), content.getLanguage(), null)).withRel("get-ethical"));
//        add(linkTo(methodOn(EthicalReportController.class).getManager(content.getId(), content.getLanguage())).withRel("get-manager"));
    }


    //    public EthicalReportListResource(EthicalReport content, EthicalSearchCondition ethicalSearchCondition, Link... links) {
//        super(content, links);
//        this.ethicalSearchCondition = ethicalSearchCondition;
//        add(linkTo(methodOn(EthicalReportController.class).ethicalGet(content.getId(), content.getLanguage())).withRel("get-ethical"));
//        add(linkTo(methodOn(EthicalReportController.class).getManager(content.getId(), content.getLanguage())).withRel("get-manager"));
//    }
}
