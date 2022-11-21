package com.daesung.api.history.web.dto;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.utils.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class recordResponseDto{

    private HistoryRecord historyRecord;

    private List<HistoryRecordFile>  historyRecordFileList;

//    private Search search;

}
