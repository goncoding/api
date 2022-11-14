package com.daesung.api.contact.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Cucheck {

    Y("확인"), N("미확인");

    private String description;

}
