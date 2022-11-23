package com.daesung.api.ir.resource;

import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.web.IrInfoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class IrInfoListResource extends EntityModel<IrInfo> {

    public IrInfoListResource(IrInfo content, Link... links) {
        super(content, links);
        //todo 등록, 삭제 추가함...
        add(linkTo(methodOn(IrInfoController.class).irInfoDelete(content.getId(), content.getLanguage())).withRel("delete-ir-info"));
    }
}
