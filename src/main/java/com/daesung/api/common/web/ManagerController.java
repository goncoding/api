package com.daesung.api.common.web;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Department;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.DepartmentRepository;
import com.daesung.api.common.repository.ManagerRepository;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.common.web.dto.FieldNameAndMnNumDto;
import com.daesung.api.common.web.dto.ManagerDto;
import com.daesung.api.common.web.dto.MnNumDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/manager")
public class ManagerController {

    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;
    private final BusinessFieldRepository businessFieldRepository;

    /**
     * 매니저 list 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getManagerListGet(@PathVariable(name = "lang", required = true) String lang) {

        List<Manager> managerList = managerRepository.findAll();
        if (managerList == null) {
            log.error("status = {}, message = {}", "404", "매니저 저장 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("매니저 저장 정보가 없습니다.","404"));
        }
        return ResponseEntity.ok().body(managerList);
    }

    /**
     * 매니저 단건 조회
     */
    @GetMapping(value = "/{num}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity managerGet(@PathVariable(name = "num") String num,
                                     @PathVariable(name = "lang", required = true) String lang) {

        Optional<Manager> optionalManager = managerRepository.findByMnNum(num);
        if (!optionalManager.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 매니저 정보가 없습니다. 사번을 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저 정보가 없습니다. 사번을 확인해주세요.","400"));
        }
        Manager manager = optionalManager.get();

        return ResponseEntity.ok(manager);

    }

     /**
     * 비지니스 필드 -> 매니저 단건 조회
     */
    @GetMapping(value = "/busField/{num}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity busFieldAndManagerGet(@PathVariable(name = "num") String num,
                                                @PathVariable(name = "lang", required = true) String lang) {

        ArrayList<String> mnNumList = new ArrayList<>();

        Optional<BusinessField> optionalBusinessField = businessFieldRepository.findByBusFieldNum(num);
        if (!optionalBusinessField.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 사업분야 정보가 없습니다. 번호를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 사업분야 정보가 없습니다. 사업번호를 확인해주세요.","400"));
        }
        BusinessField businessField = optionalBusinessField.get();

        List<Manager> managerList = managerRepository.findByBusinessField(businessField);


        for (Manager manager : managerList) {
            String mnNum = manager.getMnNum();
            mnNumList.add(mnNum);
        }

        FieldNameAndMnNumDto mnNumDto = new FieldNameAndMnNumDto();
        mnNumDto.setBusFieldName(businessField.getBusFieldName());
        mnNumDto.setMnNum(mnNumList);

        MnNumDto numDto = new MnNumDto(mnNumDto);

        return ResponseEntity.ok(numDto);
    }

    /**
     * 비지니스 필드 -> 매니저 단건 조회
     */
    @GetMapping(value = "/dept/{num}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity departmentAndManagerGet(@PathVariable(name = "num") String num,
                                                @PathVariable(name = "lang", required = true) String lang) {

        ArrayList<String> mnNumList = new ArrayList<>();

        Optional<Department> optionalDepartment = departmentRepository.findByDeptNum(num);
        if (!optionalDepartment.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 부서 번호가 없습니다. 번호를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 부서 번호가 없습니다. 번호를 확인해주세요","400"));
        }
        Department department = optionalDepartment.get();

        Optional<Manager> optionalManager = managerRepository.findByMnDepartment(department);
        if (!optionalDepartment.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 직원 번호가 없습니다. 번호를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 부서 번호가 없습니다. 번호를 확인해주세요","400"));
        }
        Manager manager = optionalManager.get();


        mnNumList.add(manager.getMnNum());


        FieldNameAndMnNumDto mnNumDto = new FieldNameAndMnNumDto();
        mnNumDto.setBusFieldName(department.getDeptName());
        mnNumDto.setMnNum(mnNumList);

        MnNumDto numDto = new MnNumDto(mnNumDto);

        return ResponseEntity.ok(numDto);
    }



    /**
     * 매니저 등록
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity managerInsert(@RequestBody ManagerDto managerDto, Errors errors,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "매니저 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        Department department = departmentRepository.findByDeptNumber(managerDto.getMnDeptNum());

        BusinessField businessField = businessFieldRepository.findByBusFieldNumber(managerDto.getBusFieldNum());

        Manager manager = new Manager();
        manager.insertManager(managerDto, lang);

        if (department != null) {
            manager.setDeptName(department.getDeptName());
            manager.setMnDepartment(department);
        }

        if (businessField != null) {
            manager.setBusinessField(businessField);
            manager.setBusinessFieldName(businessField.getBusFieldName());
        }

        Manager savedManager = managerRepository.save(manager);
        return ResponseEntity.ok(savedManager);
    }

    /**
     * 매니저 단건 삭제
     */
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity managerDelete(@PathVariable(name = "id", required = false) Long id,
                                              @PathVariable(name = "lang", required = true) String lang){

        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (!optionalManager.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 매니저 정보가 없습니다. 사번을 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저 정보가 없습니다. 사번을 확인해주세요.","400"));
        }
        Manager manager = optionalManager.get();

        managerRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(id+"번 매니저 정보 삭제 성공");
    }




}
