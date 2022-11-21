package com.daesung.api.ethical.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErCheck {

    Y("확인"), N("미확인");

    private String description;

}
