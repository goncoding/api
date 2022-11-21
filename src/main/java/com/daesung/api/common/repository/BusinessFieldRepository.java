package com.daesung.api.common.repository;

import com.daesung.api.common.domain.Business;
import com.daesung.api.common.domain.BusinessField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessFieldRepository extends JpaRepository<BusinessField, Long> {

    Optional<BusinessField> findByBusFieldName(String busFieldName);

}
