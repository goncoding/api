package com.daesung.api.common.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessengerResponse {


    private String in_recvNews;
    private String in_sawonNo;
    private String in_sendMsg;


}
