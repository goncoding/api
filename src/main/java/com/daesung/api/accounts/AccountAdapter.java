//package com.daesung.api.accounts;
//
//import com.daesung.api.accounts.domain.Account;
//import com.daesung.api.accounts.domain.enumType.AccountRole;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Getter
//public class AccountAdapter extends User {
//
//    private Account account;
//
//    public AccountAdapter(Account account) {
//        super(account.getLoginId(), account.getLoginPwd(), authorites(account.getRoles()));
//        this.account = account;
//    }
//
//    private static Collection<? extends GrantedAuthority> authorites(Set<AccountRole> roles) {
//        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toSet());
//    }
//}
