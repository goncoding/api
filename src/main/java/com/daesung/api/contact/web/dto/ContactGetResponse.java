package com.daesung.api.contact.web.dto;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.web.dto.AccountResponseDto;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.contact.domain.ContactUs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactGetResponse {

    private ContactUs contactUs;

    private AccountResponseDto accountResponseDto;

    private List<Manager> managerList;

}
