package com.daesung.api.common.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerDto {

    private Long id;
    private String mnNum;
    private String mnCategory;
    private String mnName;
    private String mnDepartment;
    private String mnPosition;
    private String mnPhone;
    private String mnEmail;
    private String regDate;
    private String language;


}
