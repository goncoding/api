package com.daesung.api.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest-template")
@RequiredArgsConstructor
public class RestTemplateController {

    private final RestTemplateService restTemplateService;


    @GetMapping("/around-hub")
    public String getHub() {

        return restTemplateService.getAroundHub();
    }

    @GetMapping("/name")
    public String name() {

        return restTemplateService.getName();
    }

    @GetMapping("/name2")
    public String name2() {

        return restTemplateService.getName2();
    }

    @GetMapping("/postDto")
    public ResponseEntity<MemberDto> postDto() {

        return restTemplateService.postDto();
    }

    @GetMapping("/addHeader")
    public ResponseEntity<MemberDto> addHeader() {

        return restTemplateService.addHeader();
    }









}
