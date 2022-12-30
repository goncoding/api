package com.daesung.api.common.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldNameAndMnNumDto {

    private List<String> mnNum;
    private String busFieldName;

}
