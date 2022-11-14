package com.daesung.api.common.repository;

import com.daesung.api.common.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByMnNum(String mnNum);

}
