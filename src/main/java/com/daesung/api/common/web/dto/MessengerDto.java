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
public class MessengerDto {

    @NotBlank(message = "발신인은 필수입니다.")
    private String inRecvNews ;
    @NotBlank(message = "발신 메일은 필수입니다.")
    private String inSawonNo;
    @NotBlank(message = "답신(문의자) 메일은 필수입니다.")
    private String inSendMsg;

}
