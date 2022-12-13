package com.daesung.api.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server")
@Slf4j
public class ServerController {

    @GetMapping(value = "/around-hub")
    public String getTest1() {
        System.out.println("getTest1() 호출 ");
        return "Hello. Around getTest1 ";
    }

    @GetMapping(value = "/name")
    public String getTest2(@RequestParam String name) {
        System.out.println("getTest2() 호출 ");
        return "Hello. "+name+" getTest2 ";
    }

    @GetMapping(value = "/path-variable/{name}")
    public String getTest3(@PathVariable("name") String name) {
        System.out.println("getTest3() 호출 ");
        return "Hello. "+name+" getTest3 ";
    }

    @PostMapping(value = "/member")
    public ResponseEntity<MemberDto> getMember(@RequestBody MemberDto memberDto,
                                               @RequestParam String name,
                                               @RequestParam String email,
                                               @RequestParam String organization) {

        System.out.println("getMember 호출 ");
        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }


    @PostMapping("/add-header")
    public ResponseEntity<MemberDto> addHeader(@RequestHeader("around-header") String header, @RequestBody MemberDto memberDto) {

        System.out.println("header = " + header);

        return ResponseEntity.status(HttpStatus.OK).body(memberDto);

    }







}
