package com.daesung.api.accounts.web;

import com.daesung.api.accounts.CurrentUser;
import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.repository.AccountRepository;
//import com.daesung.api.accounts.service.AccountService;
import com.daesung.api.accounts.service.AccountService;
import com.daesung.api.accounts.web.dto.AccountDto;
import com.daesung.api.accounts.web.dto.AccountInsertResponse;
import com.daesung.api.accounts.web.dto.AccountResponseDto;
import com.daesung.api.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;


    /**
     * Account 계정 list
     */
    @GetMapping
    public ResponseEntity AccountList(@PathVariable(name = "lang", required = true) String lang){

        List<Account> all = accountRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/info/{username}")
    public ResponseEntity AccountInfoGet(@PathVariable(name = "username") String username,
                                         @PathVariable(name = "lang", required = true) String lang,
                                         @CurrentUser Account currentUser) {

        if (currentUser == null) {
            log.error("status = {}, message = {}", "401", "접근 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("접근 권한이 없습니다.","401"));
        }

        String loginId = currentUser.getLoginId();

        Account account = accountRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException(username));


        Set<AccountRole> roles = account.getRoles();
        AccountRole accountRole = null;
        for (AccountRole role : roles) accountRole = AccountRole.valueOf(role.name());

//        Set<AccountRole> currentUserRoles = currentUser.getRoles();
//        AccountRole currentRole = null;
//        for (AccountRole role : currentUserRoles) currentRole = AccountRole.valueOf(role.name());
//
//        if (!accountRole.equals(currentRole)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("해당 권한이 없습니다.","404"));
//        }

        AccountResponseDto responseDto = AccountResponseDto.builder()
                .loginId(account.getLoginId())
                .acName(account.getAcName())
                .acEmail(account.getAcEmail())
                .accountRole(accountRole)
                .build();

        return ResponseEntity.ok(account);
    }


    /**
     * Account 계정 등록
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity AccountRegister(@RequestBody @Valid AccountDto accountDto,
                                          Errors errors,
                                          @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "계정 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<Account> optionalId = accountRepository.findByLoginId(accountDto.getLoginId());
        if (optionalId.isPresent()) {
            log.error("status = {}, message = {}", "400", "동일한 Login id가 있습니다. 변경 해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("동일한 Login id가 있습니다. 변경 해주세요.","400"));
        }

//        Optional<Account> optionalNum = accountRepository.findByAcNum(accountDto.getAcNum());
//        if (optionalNum.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("동일한 직원번원(ac_num)가 있습니다. 변경 해주세요.","400"));
//        }

        Account account = Account.builder()
                .loginId(accountDto.getLoginId())
                .loginPwd(accountDto.getLoginPwd())
//                .acNum(accountDto.getAcNum())
                .acName(accountDto.getAcName())
                .acEmail(accountDto.getAcEmail())
                .roles(accountDto.getRoles())
                .build();

        Account savedAccount = accountService.saveAccount(account);

        AccountInsertResponse insertResponse = AccountInsertResponse.builder()
                .id(savedAccount.getId())
                .loginId(savedAccount.getLoginId())
                .acName(savedAccount.getAcName())
                .acEmail(savedAccount.getAcEmail())
                .roles(savedAccount.getRoles())
                .regDate(savedAccount.getRegDate())
                .build();

        return ResponseEntity.ok(insertResponse);

    }

}
