package com.daesung.api.history.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HrCategory {

    NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");

    private String description;
}
