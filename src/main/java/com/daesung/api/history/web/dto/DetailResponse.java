package com.daesung.api.history.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DetailResponse {

    private  String year;
    private  String month;
    private List<String> contentList;


}
