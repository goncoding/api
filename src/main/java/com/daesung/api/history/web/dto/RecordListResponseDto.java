package com.daesung.api.history.web.dto;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.utils.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordListResponseDto {

    private PagedModel<EntityModel<HistoryRecord>> pagedModel;

    private Search search;

}
