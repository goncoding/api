package com.daesung.api.config;

import com.querydsl.core.annotations.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//oath2 서버와 연동되어서 사용됨..
//access 토큰을 가지고 인증 정보가 있는지 없는지를 여기서 확인... 접근 제한을 한다...
//event를 제공하는 서버와 같이 있는게 맞고 인증서버는 같이 써도 상관없다..

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
     resources.resourceId("daesung");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
            http
                .anonymous()
                    .and()
                .authorizeRequests()
                    .mvcMatchers("/editor-upload/**").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/kr/manager/busField/**","/kr/manager/dept/**","/kr/esg/view/**","/kr/esg","/kr/ir-info/down/**","/kr/disclosure-info/down/**","/kr/dart","/kr/account/**","/kr/news/**","/kr/history","/kr/history/detail","/en/history","/en/history/detail","/kr/ir-info","/kr/disclosure-info","/kr/popup/**","/upload/**","/kr/ir-year","/getNiceDeSet").permitAll()
                    .mvcMatchers(HttpMethod.POST,"/kr/contact","/kr/ethical","/kr/account","/kr/mail/**","/getNiceCryptoToken","/msg/mail").permitAll()
                    .mvcMatchers( "/kr/contact/**","/msg/**").hasAnyRole("ADMIN","PERSON","INFO","DS_HEAD","SEOUL_OIL","DAEGU_OIL","MACHINERY","OVERSEAS_RESOURCE","DS_CELTICS","DS_HEAT","DS_METERS","KR_CAMBRIDGE","DS_HYDRAULIC","DS_LOGISTICS","D_CUBE_GEOJE","DS_CS","DS_POWER","KR_LOGISTICS","DS_ART")
                    .mvcMatchers( "/kr/bus-field/**","/kr/popup/**","/kr/esg/**","/kr/history/**","/en/history/**","/kr/news/**").hasAnyRole("ADMIN","PERSON")
                    .mvcMatchers( "/kr/ir-year/**","/kr/ir-info/**","/kr/disclosure-info/**").hasAnyRole("ADMIN","FINANCE")
                    .mvcMatchers( "/kr/ethical/**").hasAnyRole("ADMIN","AUDIT")
                    .mvcMatchers("/kr/**","/en/**").hasAnyRole("ADMIN")
//                    .mvcMatchers(HttpMethod.GET, "/kr/**").hasRole("ADMIN")
//                    .mvcMatchers(HttpMethod.GET, "/en/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
//                    .anyRequest().permitAll()
                    .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());

    }
}
