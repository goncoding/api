//package com.daesung.api.accounts.web;
//
//import com.daesung.api.accounts.domain.Account;
//import com.daesung.api.accounts.domain.enumType.AccountRole;
//import com.daesung.api.accounts.repository.AccountRepository;
////import com.daesung.api.accounts.service.AccountService;
//import com.daesung.api.accounts.web.dto.AccountDto;
//import com.daesung.api.common.response.ErrorResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.hateoas.MediaTypes;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/{lang}/account")
//public class AccountController {
//
//    private final AccountRepository accountRepository;
////    private final AccountService accountService;
//
//
//
////    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
////    public ResponseEntity AccountRegister(@RequestBody @Valid AccountDto accountDto,
////                                          Errors errors,
////                                          @PathVariable(name = "lang", required = true) String lang) {
////
////        if (errors.hasErrors()) {
////            return ResponseEntity.badRequest().body(errors);
////        }
////
////        Optional<Account> optionalId = accountRepository.findByLoginId(accountDto.getLoginId());
////        if (optionalId.isPresent()) {
////            return ResponseEntity.badRequest().body(new ErrorResponse("동일한 Login id가 있습니다. 변경 해주세요.","400"));
////        }
////
////        Optional<Account> optionalNum = accountRepository.findByAcNum(accountDto.getAcNum());
////        if (optionalNum.isPresent()) {
////            return ResponseEntity.badRequest().body(new ErrorResponse("동일한 직원번원(ac_num)가 있습니다. 변경 해주세요.","400"));
////        }
////
////
////        Account account = Account.builder()
////                .loginId(accountDto.getLoginId())
////                .loginPwd(accountDto.getLoginPwd())
////                .acNum(accountDto.getAcNum())
////                .acName(accountDto.getAcName())
////                .roles(accountDto.getRoles())
////                .build();
////
////        Account savedAccount = accountService.saveAccount(account);
////
////        return ResponseEntity.ok(savedAccount);
////
////    }
//
//}
