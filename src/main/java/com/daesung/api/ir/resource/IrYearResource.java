package com.daesung.api.ir.resource;

import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ir.domain.IrYear;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class IrYearResource extends EntityModel<IrYear> {

    public IrYearResource(IrYear content, Link... links) {
        super(content, links);
    }
}
