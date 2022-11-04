package com.daesung.api.news.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NbType {

    NE("뉴스"), RE("보도");

    private String description;

}
