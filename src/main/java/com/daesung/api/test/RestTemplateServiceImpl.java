package com.daesung.api.test;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateServiceImpl implements RestTemplateService{


    @Override
    public String getAroundHub() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/api/server/around-hub")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    @Override
    public String getName() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/api/server/name")
                .queryParam("name","goni")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);


        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    @Override
    public String getName2() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/api/server/path-variable/{name}")
                .encode()
                .build()
                .expand("path-goni")
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);


        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    @Override
    public ResponseEntity<MemberDto> postDto() {

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/api/server/member")
                .queryParam("name","name1")
                .queryParam("email","email1")
                .queryParam("organization","organization1")
                .encode()
                .build()
                .toUri();

        MemberDto memberDto = MemberDto.builder()
                .name("aaa!")
                .email("aaa@naver.com!")
                .organization("around hub sss!")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDto> responseEntity =
                restTemplate.postForEntity(uri, memberDto, MemberDto.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return responseEntity;
    }

    @Override
    public ResponseEntity<MemberDto> addHeader() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/")
                .path("/api/server/add-header")
                .encode()
                .build()
                .toUri();

        MemberDto memberDto = MemberDto.builder()
                .name("aaa!")
                .email("aaa@naver.com")
                .organization("around hub sss")
                .build();

        RequestEntity<MemberDto> requestEntity = RequestEntity
                .post(uri)
                .header("around-header", "around hub header")
                .body(memberDto);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDto> responseEntity = restTemplate.exchange(requestEntity, MemberDto.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return responseEntity;
    }
}
