package com.daesung.api.ir.repository;

import com.daesung.api.ir.domain.IrYear;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface IrYearRepository extends JpaRepository<IrYear, Long> {

    Optional<IrYear> findByIyYear(String year);

}
