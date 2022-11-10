package com.daesung.api.news.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShowYn {

    Y("보임"), N("안보임");

    private String description;

}
