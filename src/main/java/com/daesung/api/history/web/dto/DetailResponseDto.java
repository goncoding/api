package com.daesung.api.history.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailResponseDto {

    private String hdYear;
    private String hdMonth;
    private List<String> contentList;


}
