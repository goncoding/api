package com.daesung.api.accounts.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {
    DS_POWER("DS파워"), DS_ENERGY("대성히트에너시스");

    private String description;


}
