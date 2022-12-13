package com.daesung.api.nice.repository;

import com.daesung.api.nice.domain.NiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiceCertRepository extends JpaRepository<NiceInfo, Long> {
    NiceInfo findByReqNo(String keyNo);
}
