package com.daesung.api.common.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MnCategory {

    DS_POWER("DS파워"), DS_ENERGY("대성히트에너시스"), DS_RELAY("대성계전");

    private String description;


}
