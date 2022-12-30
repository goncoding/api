package com.daesung.api.config;

import com.daesung.api.accounts.properties.AdminProperties;
import com.daesung.api.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager; //유저 정보를 가지고 있음
    @Autowired
    AccountService accountService;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    AdminProperties adminProperties;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //client와 secret을 통해 해당 client가 맞는지 검증..
                .withClient(adminProperties.getClientId()) // client secret
                .secret(passwordEncoder.encode(adminProperties.getClientSecret()))// client secret
                .authorizedGrantTypes("password","refresh_token")//속성지정이 안되어있으면 인증 불가
                .scopes("read","write") //해당 client로 접근했을 때 접근 범위를 제한시키는 속성
                .accessTokenValiditySeconds(60 * 60 * 12)
                .refreshTokenValiditySeconds(60 * 60 * 24 * 30);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);//token 저장

    }
}
