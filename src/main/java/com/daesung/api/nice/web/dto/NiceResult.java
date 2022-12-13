package com.daesung.api.nice.web.dto;

import lombok.Data;

@Data
public class NiceResult {

    private String keyNo;
    private String token_version_id;
    private String enc_data;
    private String integrity_value;


}
