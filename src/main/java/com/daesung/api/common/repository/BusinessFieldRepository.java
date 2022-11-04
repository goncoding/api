package com.daesung.api.common.repository;

import com.daesung.api.common.domain.Business;
import com.daesung.api.common.domain.BusinessField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessFieldRepository extends JpaRepository<BusinessField, Long> {
}
