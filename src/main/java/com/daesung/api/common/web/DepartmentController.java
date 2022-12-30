package com.daesung.api.common.web;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Department;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/dept")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    /**
     * 사업 분야 리스트 조회
     */
    @GetMapping
    public ResponseEntity businessFieldList(Pageable pageable,
                                            @PathVariable(name = "lang") String lang) {

        Page<Department> departments = departmentRepository.findAll(pageable);
        return ResponseEntity.ok(departments);
    }



}
