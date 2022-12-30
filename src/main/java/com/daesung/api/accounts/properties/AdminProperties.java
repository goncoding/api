package com.daesung.api.accounts.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "ds-account")
@Getter @Setter
public class AdminProperties {

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;

    @NotEmpty
    private String userUsername;
    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String adminUsername;
    @NotEmpty
    private String adminPassword;

    //정보 시스템 사업부
    @NotEmpty
    private String infoUsername;
    @NotEmpty
    private String infoPassword;

    //인사 총무부
    @NotEmpty
    private String personUsername;
    @NotEmpty
    private String personPassword;

    //재무 IR기획부
    @NotEmpty
    private String financeUsername;
    @NotEmpty
    private String financePassword;

    //감사실
    @NotEmpty
    private String auditUsername;
    @NotEmpty
    private String auditPassword;



}
