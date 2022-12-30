package com.daesung.api.common.web;

import com.daesung.api.accounts.CurrentUser;
import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.repository.AccountRepository;
import com.daesung.api.accounts.web.dto.AccountResponseDto;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.common.web.dto.MailDto;
import com.daesung.api.common.web.dto.MessengerDto;
import com.daesung.api.common.web.dto.MessengerResponse;
import com.daesung.api.common.web.dto.UserMailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/msg")
public class MailController {


    private final JavaMailSender javaMailSender;

    private final AccountRepository accountRepository;

    /**
     *  사용자 메일 전송
     */
    @PostMapping("/mail")
    public ResponseEntity sendMail(@RequestBody @Valid UserMailDto userMailDto, Errors errors,
                                   @CurrentUser Account account) {

        Set<AccountRole> roles = account.getRoles();
        AccountRole currentRole = null;
        for (AccountRole role : roles) currentRole = AccountRole.valueOf(role.name());

        Optional<Account> optionalAccount = accountRepository.findByLoginId(account.getLoginId());
        if (!optionalAccount.isPresent()) {
            log.error("status = {}, message = {}", "403", "login 이후 진행 해주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("login 이후 진행 해주세요.","401"));
        }
        Account currentAccount = optionalAccount.get();


        SimpleMailMessage message = new SimpleMailMessage();
        String receiver = userMailDto.getReceiver();
        log.error("mail receiver =========>>>> {}" , receiver);

        String acEmail = currentAccount.getAcEmail();
        log.error("mail acEmail =========>>>> {}" , acEmail);

        String subject = userMailDto.getSubject();
        log.error("mail subject =========>>>> {}" , subject);

        String content = userMailDto.getContent();
        log.error("mail content =========>>>> {}" , content);

        message.setTo(receiver);
        message.setFrom(acEmail);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);

        System.out.println(" 메일전송 성공 ");

//        ArrayList<String> list = new ArrayList<>();
//
//        list.add("soro.java@gmail.com");
//        list.add("deokgoni@naver.com");
//
//        int size = list.size();
//
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo((String[]) list.toArray(new String[size]));
//        simpleMailMessage.setSubject("subject sample");
//        simpleMailMessage.setText("text sample");
//
//        javaMailSender.send(simpleMailMessage);
//
//        System.out.println("메일전송 성공 = " + simpleMailMessage);

        return ResponseEntity.ok().build();
    }

    /**
     *  그룹 웨어 메일 전송
     */
    @PostMapping(value = "/groupware/mail", produces = MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity groupwareMailPost(@RequestBody @Valid MailDto mailDto, Errors errors) {

        if (errors.hasErrors()) {
            log.info("status = {}, message = {}", "400", "그룹 웨어 메일 전송 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://gwmm.nleader.co.kr")
                .path("/covicore/control2/sendMailSimple.do")
                .encode().build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MailDto> responseEntity = restTemplate.postForEntity(uri, mailDto, MailDto.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return ResponseEntity.ok(responseEntity);
    }


    /**
     *  그룹 웨어 메일 전송
     */
//    @PostMapping(value = "/groupware/mail", produces = MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8)
//    public ResponseEntity groupwareMailPost(@RequestBody @Valid MailDto mailDto, Errors errors) {
//
//        if (errors.hasErrors()) {
//            log.info("status = {}, message = {}", "400", "그룹 웨어 메일 전송 필수 값을 확인 해 주세요.");
//            return ResponseEntity.badRequest().body(errors);
//        }
//
//        URI uri = UriComponentsBuilder
//                .fromUriString("https://gwmm.nleader.co.kr")
//                .path("/covicore/control2/sendMailSimple.do")
//                .encode().build().toUri();
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<MailDto> responseEntity = restTemplate.postForEntity(uri, mailDto, MailDto.class);
//
//        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
//        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());
//
//        return ResponseEntity.ok(responseEntity);
//    }

    /**
     *  그룹 웨어 메신저 전송
     */
    @PostMapping(value = "/groupware/messenger", produces = MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity groupwareMessengerPost(@RequestBody @Valid MessengerDto messengerDto, Errors errors) {

       if (errors.hasErrors()) {
           log.info("status = {}, message = {}", "400", "그룹 웨어 메신저 전송 필수 값을 확인 해 주세요.");
           return ResponseEntity.badRequest().body(errors);
       }

        URI uri = UriComponentsBuilder
                .fromUriString("https://gw.nleader.co.kr")
                .path("/covicore/control2/insertTionMessage.do")
                .encode().build().toUri();

        MessengerResponse messengerResponse = MessengerResponse.builder()
                .in_recvNews(messengerDto.getInRecvNews())
                .in_sawonNo(messengerDto.getInSawonNo())
                .in_sendMsg(messengerDto.getInSendMsg())
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MessengerResponse> responseEntity = restTemplate.postForEntity(uri, messengerResponse, MessengerResponse.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return ResponseEntity.ok(responseEntity);
    }


    @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity mailPostV2(@RequestBody @Valid MailDto mailDto) {

        URI uri = UriComponentsBuilder
//                .fromUriString("https://gw.nleader.co.kr/covicore/control2/sendMailSimple.do")
                .fromUriString("https://gw.nleader.co.kr")
                .path("/covicore/control2/sendMailSimple.do")
                .encode().build().toUri();

        RequestEntity<MailDto> requestEntity = RequestEntity.post(uri)
                .header("Content-Type", "application/json")
                .body(mailDto);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MailDto> responseEntity = restTemplate.exchange(requestEntity, MailDto.class);

        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return ResponseEntity.ok(responseEntity);
    }




}


