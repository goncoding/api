package com.daesung.api.common.web;

import com.daesung.api.common.domain.Business;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.BusinessRepository;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.common.web.dto.BusinessFieldResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/bus-field")
public class BusinessFieldController {

    private final BusinessRepository businessRepository;
    private final BusinessFieldRepository businessFieldRepository;

    /**
     * 사업 분야 리스트 조회
     */
    @GetMapping
    public ResponseEntity businessFieldList(Pageable pageable,
                                            @PathVariable(name = "lang") String lang) {

        Page<BusinessField> fieldPage = businessFieldRepository.findAll(pageable);
        return ResponseEntity.ok(fieldPage);
    }

    /**
     * 사업 분야 넘버 단건 조회
     */
//    @GetMapping("/{num}")
//    public ResponseEntity businessFieldNumGet(@PathVariable(name = "num") String num,
//                                           @PathVariable(name = "lang") String lang) {
//
//        Optional<BusinessField> optionalBusinessField = businessFieldRepository.findByBusFieldNum(num);
//        if (!optionalBusinessField.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 사업분야가 없습니다. 사업분야 번호를 확인 해주세요.","400"));
//        }
//        BusinessField businessField = optionalBusinessField.get();
//
//        Optional<Business> optionalBusiness = businessRepository.findById(businessField.getBusiness().getId());
//        Business business = optionalBusiness.get();
//
//        BusinessFieldResponse fieldResponse = BusinessFieldResponse.builder()
//                .businessField(businessField)
//                .businessDto(new BusinessDto(business))
//                .build();
//
//        return ResponseEntity.ok(fieldResponse);
//    }






}
