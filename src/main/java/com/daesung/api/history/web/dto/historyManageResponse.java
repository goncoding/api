package com.daesung.api.history.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class historyManageResponse {

    private String hdYear;
    private String hdMonth;

    private List<String> contentList;
    private Integer hdSequence;
    private String language;
//    private String regUser;
    private String updUser;



}
