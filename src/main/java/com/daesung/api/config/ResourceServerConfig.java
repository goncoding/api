//package com.daesung.api.config;
//
//import com.querydsl.core.annotations.Config;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
////oath2 서버와 연동되어서 사용됨..
////access 토큰을 가지고 인증 정보가 있는지 없는지를 여기서 확인... 접근 제한을 한다...
////event를 제공하는 서버와 같이 있는게 맞고 인증서버는 같이 써도 상관없다..
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//     resources.resourceId("daesung");
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//            http
//                .anonymous()
//                    .and()
//                .authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET, "/kr/**").permitAll()
//                    .mvcMatchers("/kr/account").permitAll()
////                    .anyRequest().authenticated()
//                    .anyRequest().permitAll()
//                    .and()
//                .exceptionHandling()
//                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
//    }
//}
