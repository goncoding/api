package com.daesung.api.contact.repository;

import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.repository.condition.ContactSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactUsRepositoryCustom {

    Page<ContactUs> searchContactList(ContactSearchCondition searchCondition, Pageable pageable);

}
