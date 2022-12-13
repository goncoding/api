package com.daesung.api.nice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NiceCallResponse {


    private String token_version_id; //암호화토큰요청_API 응답으로 받은 값
    private String enc_data; //암호화한 요청정보
    private String integrity_value; //enc_data의 무결성 값


}
