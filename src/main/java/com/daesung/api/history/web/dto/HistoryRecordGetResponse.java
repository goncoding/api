package com.daesung.api.history.web.dto;

import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecordGetResponse {

    private HistoryRecord historyRecord;

    private List<HistoryRecordFile> historyRecordFileList;

    private HistoryRecord prevRecord;

    private HistoryRecord nextRecord;


}
