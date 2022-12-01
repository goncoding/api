package com.daesung.api.contact.web.dto;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.contact.domain.ContactUs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactUsResponse {

    private ContactUs contactUs;

    private ContactBusFieldDto busFieldDto;

}
