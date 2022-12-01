package com.daesung.api.common.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsentStatus {

    Y("동의"), N("미동의");

    private String description;

}
