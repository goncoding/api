package com.daesung.api.ir.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/dart")
public class DartController {

    public static final String API_AUTHENTICATION_KEY = "0eec51ed76e76829f4134add98625b2ac44d9d99";
    public static final String UNIQUE_NUMBER = "00828789";
    public static final String FINAL_REPORT_YN = "Y";

    private final ObjectMapper objectMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity dartList(@RequestParam(required = false, defaultValue = "10") String page_count,
                           @RequestParam(required = false) String bgn_de,
                           @RequestParam(required = false) String end_de,
                           @RequestParam(required = false, defaultValue = "0") String page_no) {

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String today = simpleDate.format(time);
        System.out.println("today = " + today);
//
//        int year  = Integer.parseInt(today.substring(0, 4));
//        int month = Integer.parseInt(today.substring(4, 6));
//        int date  = Integer.parseInt(today.substring(6, 8));
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(year, month - 1, date);
//
//        cal.add(Calendar.YEAR, -1);     // 1년 전
//
//        String lastYear = simpleDate.format(cal.getTime());
//        System.out.println("lastYear = " + lastYear);
// https://dart.fss.or.kr/dsaf001/main.do?rcpNo=

        URI uri = UriComponentsBuilder
                .fromUriString("https://opendart.fss.or.kr")
                .path("/api/list.json")
                .queryParam("crtfc_key", API_AUTHENTICATION_KEY)
                .queryParam("corp_code", UNIQUE_NUMBER)
                .queryParam("last_reprt_at", FINAL_REPORT_YN)
                .queryParam("bgn_de", "20100101")
                .queryParam("end_de", today)
                .queryParam("page_count", page_count)
                .queryParam("page_no", page_no)
                .encode()
                .build()
                .toUri();

        System.out.println("dart uri ================ " + uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        System.out.println("dart responseEntity ================ " + responseEntity);

        return ResponseEntity.ok(responseEntity.getBody());
    }



}
