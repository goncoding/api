package com.daesung.api.history.web.dto;

import com.daesung.api.history.domain.enumType.HrCategory;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class RecordDto {

    @NotBlank@NotBlank(message = "카테고리는 필수입니다.")
    private String hrCategory;
    @NotBlank(message = "제목은 필수입니다.")
    private String hrCategoryName;
    private String hrTitle;
    private String hrContent;



}
