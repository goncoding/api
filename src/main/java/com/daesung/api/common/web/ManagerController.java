package com.daesung.api.common.web;

import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.ManagerRepository;
import com.daesung.api.common.resource.ManagerResource;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.resource.HistoryListResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/manager")
public class ManagerController {

    private final ManagerRepository managerRepository;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getManagerListGet(@PathVariable(name = "lang", required = true) String lang) {

        List<Manager> managerList = managerRepository.findAll();
        if (managerList == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저리스트 정보가 없습니다.","400"));
        }
        return ResponseEntity.ok().body(managerList);
    }

    @GetMapping(value = "/{num}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity getManager(@PathVariable(name = "num") String num,
                                     @PathVariable(name = "lang", required = true) String lang) {

        Optional<Manager> optionalManager = managerRepository.findByMnNum(num);
        if (!optionalManager.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 매니저 정보가 없습니다. 사번을 확인해주세요."));
        }

        return ResponseEntity.ok(optionalManager.get());
    }


}
