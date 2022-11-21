package com.daesung.api.history.resource;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.web.HistoryRecordController;
import com.daesung.api.utils.search.Search;
import com.daesung.api.utils.search.SearchDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class HistoryRecordResource extends EntityModel<HistoryRecord> {

    private SearchDto searchDto;

    public HistoryRecordResource(HistoryRecord content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(HistoryRecordController.class).recordGet(content.getId(), content.getLanguage())).withSelfRel());
    }

//    public HistoryRecordResource(HistoryRecord content, SearchDto searchDto, Link... links) {
//        super(content, links);
//        this.searchDto = searchDto;
//    }
}
