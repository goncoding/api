package com.daesung.api.contact.repository;

import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.repository.condition.ContactSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {

    Page<ContactUs> searchContactList(ContactSearchCondition searchCondition, Pageable pageable);


}
