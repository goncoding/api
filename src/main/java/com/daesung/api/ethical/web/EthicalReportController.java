package com.daesung.api.ethical.web;

import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.ManagerRepository;
import com.daesung.api.common.resource.ManagerDtoResource;
import com.daesung.api.common.resource.ManagerResource;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.common.web.dto.ManagerDto;
import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.resource.ContactUsListResource;
import com.daesung.api.contact.resource.ContactUsResource;
import com.daesung.api.ethical.domain.EthicalReport;
import com.daesung.api.ethical.domain.enumType.ErCheck;
import com.daesung.api.ethical.repository.EthicalReportRepository;
import com.daesung.api.ethical.repository.condition.EthicalSearchCondition;
import com.daesung.api.ethical.resource.EthicalReportListResource;
import com.daesung.api.ethical.resource.EthicalReportResource;
import com.daesung.api.ethical.web.dto.EthicalReportDto;
import com.daesung.api.ethical.web.dto.EthicalReportUpdateDto;
import com.daesung.api.ethical.web.dto.EthicalUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/ethical")
public class EthicalReportController {

    private final EthicalReportRepository ethicalReportRepository;
    private final BusinessFieldRepository businessFieldRepository;
    private final ManagerRepository managerRepository;

    /**
     * 윤리경영 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity ethicalListGet(Pageable pageable,
                                         PagedResourcesAssembler<EthicalReport> assembler,
                                         @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                         @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                         @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                         @RequestParam(name = "size", required = false, defaultValue = "") String size) {

        EthicalSearchCondition searchCondition = new EthicalSearchCondition();
        searchCondition.setSearchType(searchType);
        searchCondition.setPage(page);
        searchCondition.setSize(size);


        if ("name".equals(searchType)) {
            searchCondition.setSearchName(searchText);
        }
        if ("content".equals(searchType)) {
            searchCondition.setSearchText(searchText);
        }
        if ("mnName".equals(searchType)) {
            searchCondition.setSearchMnName(searchText);
        }

        Page<EthicalReport> ethicalReportPage = ethicalReportRepository.searchEthicalList(searchCondition, pageable);
        PagedModel<EntityModel<EthicalReport>> pagedModel = assembler.toModel(ethicalReportPage, e -> new EthicalReportListResource(e));

//        EthicalReportListResponse response = new EthicalReportListResponse(ethicalReportPage, searchCondition);

        return ResponseEntity.ok().body(pagedModel);

    }


    /**
     *  윤리경영 단건 조회
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity ethicalGet(@PathVariable(name = "id", required = true) Long id,
                                     @PathVariable(name = "lang", required = true) String lang) {

        Optional<EthicalReport> optionalEthicalReport = ethicalReportRepository.findById(id);
        if (!optionalEthicalReport.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 윤리경영신고 문의가 없습니다.", "404"));
        }
        EthicalReport ethicalReport = optionalEthicalReport.get();

        EthicalReportResource reportResource = new EthicalReportResource(ethicalReport);
        reportResource.add(linkTo(methodOn(EthicalReportController.class).ethicalGet(id, lang)).withSelfRel());
        reportResource.add(linkTo(methodOn(EthicalReportController.class).ethicalUpdate(null, null, id, lang)).withRel("update-ethical"));
        reportResource.add(linkTo(methodOn(EthicalReportController.class).getManager(id, lang)).withRel("get-manager(er_id)"));

        return ResponseEntity.ok(reportResource);
    }

    /**
     * 윤리경영 등록
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity ethicalInsert(@RequestBody @Valid EthicalReportDto reportDto,
                                        Errors errors,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<BusinessField> busFieldName = businessFieldRepository.findByBusFieldName("윤리경영신고");
        if (!busFieldName.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("윤리경영신고 부서를 찾을 수 없습니다.", "404"));
        }

        BusinessField businessField = busFieldName.get();

        EthicalReport ethicalReport = EthicalReport.builder()
                .erName(reportDto.getErName())
                .erEmail(reportDto.getErEmail())
                .erPhone(reportDto.getErPhone())
                .erContent(reportDto.getErContent())
                .erCheck(ErCheck.N)
                .language(lang)
                .businessField(businessField)
                .build();

        EthicalReport savedReport = ethicalReportRepository.save(ethicalReport);
        EthicalReportResource ethicalReportResource = new EthicalReportResource(savedReport);

        return ResponseEntity.ok(savedReport);
    }


    /**
     * 윤리경영 수정
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity ethicalUpdate(@RequestBody @Valid EthicalReportUpdateDto updateDto, Errors errors,
                                        @PathVariable(name = "id", required = true) Long id,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<EthicalReport> optionalEthicalReport = ethicalReportRepository.findById(id);
        if (!optionalEthicalReport.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
        }
        EthicalReport ethicalReport = optionalEthicalReport.get();

        checkStatus(updateDto, ethicalReport);

        /**
         *  (답변, 매니저, 메모) 추가시 -> 매니저 선택 필수 -> 수정 가능
         */
        if (updateDto.getErAnswer() != null || updateDto.getMnNum() != null || updateDto.getErMemo() != null) {
            Optional<Manager> optionalManager = managerRepository.findByMnNum(updateDto.getMnNum());
            if (!optionalManager.isPresent()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저가 없습니다. 매니저를 선택하세요. 사번 = "+updateDto.getMnNum(),"400"));
            }
            Manager manager = optionalManager.get();
            ethicalReport.changeAnswer(updateDto, manager);
        }

        ethicalReport.changeReport(updateDto);

        EthicalReport updatedReport = ethicalReportRepository.save(ethicalReport);

        EthicalReportResource ethicalReportResource = new EthicalReportResource(updatedReport);
        ethicalReportResource.add(linkTo(methodOn(EthicalReportController.class).ethicalUpdate(null,null,ethicalReport.getId(),lang)).withSelfRel());
        ethicalReportResource.add(linkTo(methodOn(EthicalReportController.class).getManager(updatedReport.getId(), lang)).withRel("get-manager"));

        return ResponseEntity.ok(ethicalReportResource);
    }

    private static void checkStatus(EthicalReportUpdateDto updateDto, EthicalReport ethicalReport) {
        if ("Y".equals(updateDto.getErCheck())) {
            ethicalReport.setErCheck(ErCheck.Y);
        }
        if ("N".equals(updateDto.getErCheck())) {
            ethicalReport.setErCheck(ErCheck.N);
        }
    }


    /**
     *  윤리경영 삭제
     */



     /**
     *  매니저 불러오기
     */
     @GetMapping(value = "/manager/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
     public ResponseEntity getManager(@PathVariable(name = "id") Long id,
                                      @PathVariable(name = "lang", required = true) String lang) {

         Optional<EthicalReport> optionalEthicalReport = ethicalReportRepository.findById(id);
         if (!optionalEthicalReport.isPresent()) {
             return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
         }

         EthicalReport ethicalReport = optionalEthicalReport.get();


         Manager reportManager = ethicalReport.getManager();
         if (reportManager == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("담당 매니저가 없습니다.","404"));
         }

         String mnNum = reportManager.getMnNum();

         Optional<Manager> optionalManager = managerRepository.findByMnNum(mnNum);
         if (!optionalManager.isPresent()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 매니저 정보가 없습니다. 사번을 확인해주세요."));
         }

         ManagerDto managerDto = getManagerDto(optionalManager);

         ManagerDtoResource managerDtoResource = new ManagerDtoResource(managerDto, lang);

         return ResponseEntity.ok(managerDtoResource);
     }

    private static ManagerDto getManagerDto(Optional<Manager> optionalManager) {
        Manager manager = optionalManager.get();
        ManagerDto managerDto = ManagerDto.builder()
                .id(manager.getId())
                .mnNum(manager.getMnNum())
                .mnCategory(manager.getMnCategory())
                .mnName(manager.getMnName())
                .mnDepartment(manager.getMnDepartment())
                .mnPosition(manager.getMnPosition())
                .mnPhone(manager.getMnPhone())
                .mnEmail(manager.getMnEmail())
                .build();
        return managerDto;
    }


}
