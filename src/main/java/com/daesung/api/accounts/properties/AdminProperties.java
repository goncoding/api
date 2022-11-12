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
    private String AdminUsername;
    @NotEmpty
    private String AdminPassword;
    @NotEmpty
    private String PowerUsername;
    @NotEmpty
    private String PowerPassword;
    @NotEmpty
    private String EnergyUsername;
    @NotEmpty
    private String EnergyPassword;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;

}
