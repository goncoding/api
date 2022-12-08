package com.daesung.api.nice.repository;

import com.daesung.api.nice.domain.NiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiceInfoRepository extends JpaRepository<NiceInfo, Long> {
}
