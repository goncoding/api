package com.daesung.api.contact.web;

import com.daesung.api.accounts.CurrentUser;
import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.accounts.web.dto.AccountResponseDto;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.BusinessFieldRepository;
import com.daesung.api.common.repository.DepartmentRepository;
import com.daesung.api.common.repository.ManagerRepository;
import com.daesung.api.common.resource.ManagerDtoResource;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.common.web.BusinessFieldController;
import com.daesung.api.common.web.dto.ManagerDto;
import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.common.domain.enumType.ConsentStatus;
import com.daesung.api.contact.domain.enumType.Cucheck;
import com.daesung.api.contact.repository.ContactUsRepository;
import com.daesung.api.contact.repository.condition.ContactSearchCondition;
import com.daesung.api.contact.resource.ContactUsListResource;
import com.daesung.api.contact.resource.ContactUsResource;
import com.daesung.api.contact.web.dto.ContactGetResponse;
import com.daesung.api.contact.web.dto.ContactUsDto;
import com.daesung.api.contact.web.dto.ContactUsUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/{lang}/contact")
public class ContactUsController {

    private final ContactUsRepository contactUsRepository;

    private final ManagerRepository managerRepository;
    private final BusinessFieldRepository businessFieldRepository;
    private final DepartmentRepository departmentRepository;

    /**
     *  1대1 문의 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity contactListGet(Pageable pageable,
                                            PagedResourcesAssembler<ContactUs> assembler,
                                            @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                            @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                            @RequestParam(name = "size", required = false, defaultValue = "") String size,
                                            @CurrentUser Account account) {

        if (account == null) {
            log.error("status = {}, message = {}", "403", "접근 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("접근 권한이 없습니다.","401"));
        }

        Set<AccountRole> roles = account.getRoles();
        AccountRole currentRole = null;

        for (AccountRole role : roles) {
            currentRole = AccountRole.valueOf(role.name());
        }

        ContactSearchCondition searchCondition = new ContactSearchCondition();
        searchCondition.setSearchType(searchType);

        if (currentRole != null) {
            searchCondition.setCurrentRole(currentRole);
        }
        if ("name".equals(searchType)) {
            searchCondition.setSearchName(searchText);
        }
        if ("content".equals(searchType)) {
            searchCondition.setSearchText(searchText);
        }
        if ("busFieldName".equals(searchType)) {
            searchCondition.setSearchFieldName(searchText);
        }
        if ("mnName".equals(searchType)) {
            searchCondition.setSearchMnName(searchText);
        }
        if ("mnName".equals(searchType)) {
            searchCondition.setSearchMnName(searchText);
        }

        Page<ContactUs> contactUsPage = contactUsRepository.searchContactList(searchCondition, pageable);
        PagedModel<EntityModel<ContactUs>> pagedModel = assembler.toModel(contactUsPage, e -> new ContactUsListResource(e));

        return ResponseEntity.ok().body(pagedModel);
    }

    /**
     *  1대1 문의 단건 조회
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactGet(@PathVariable(name = "id", required = true) Long id,
                                     @PathVariable(name = "lang", required = true) String lang,
                                     @CurrentUser Account account){

        if (account == null) {
            log.error("status = {}, message = {}", "403", "접근 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("접근 권한이 없습니다.","401"));
        }

        Set<AccountRole> roles = account.getRoles();
        AccountRole currentRole = null;
        for (AccountRole role : roles) currentRole = AccountRole.valueOf(role.name());


        Optional<ContactUs> optionalContactUs = contactUsRepository.findById(id);
        if (!optionalContactUs.isPresent()) {
            log.error("status = {}, message = {}", "404", "일치하는 1대1 문의가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","404"));
        }
        ContactUs contactUs = optionalContactUs.get();

        //todo person 계정이 아니면서 현재 계정과 db저장 계정이 같지 않으면
        if (!currentRole.equals(contactUs.getAccountRole()) && !"PERSON".equals(currentRole.name()) && !"ADMIN".equals(currentRole.name()) ) {
            log.error("status = {}, message = {}", "400", "일치하는 1대1 문의가 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("문의 접근 권한이 없습니다. 필요 권한 = " + contactUs.getAccountRole().name() + ", 현재 권한 = " + currentRole.name(),"403"));
        }

        List<Manager> managerList = managerRepository.findByAccountRole(currentRole);

        String busFieldNum = contactUs.getBusinessField().getBusFieldNum();

        AccountResponseDto accountResponseDto = AccountResponseDto.builder()
                .loginId(account.getLoginId())
                .acName(account.getAcName())
                .acEmail(account.getAcEmail())
                .build();

        ContactGetResponse contactGetResponse = new ContactGetResponse(contactUs, accountResponseDto, managerList);

//        ContactUsResource contactUsResource = new ContactUsResource(contactUs);

//        if (contactUs.getManager() != null) {
//            contactUsResource.add(linkTo(methodOn(ContactUsController.class).getManager(id,lang)).withRel("get-manager(cu_id)"));
//        }


        return ResponseEntity.ok(contactGetResponse);
    }

    /**
     *  1대1 문의 등록 (등록시 cuAnswer, mnNum, cuMemo null 값 )
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactInsert(@RequestBody @Valid ContactUsDto contactUsDto,
                                        Errors errors,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "1대1 문의 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        if (contactUsDto.getConsentStatus() != ConsentStatus.Y) {
            log.error("status = {}, message = {}", "400", "개인정보에 동의 해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("개인정보에 동의 해주세요.","404"));
        }

        Optional<BusinessField> optionalBusinessField = businessFieldRepository.findByBusFieldNum(contactUsDto.getBusFieldNum());
        if (!optionalBusinessField.isPresent()) {
            log.error("status = {}, message = {}", "404", "일치하는 사업분야가 없습니다. 사업분야 번호를 확인 해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 사업분야가 없습니다. 사업분야 번호를 확인 해주세요.","404"));
        }
        BusinessField businessField = optionalBusinessField.get();

        ContactUs contactUs = ContactUs.builder()
                .cuName(contactUsDto.getCuName())
                .cuEmail(contactUsDto.getCuEmail())
                .cuPhone(contactUsDto.getCuPhone())
                .cuContent(contactUsDto.getCuContent())
                .consentStatus(ConsentStatus.Y)
                .businessField(businessField)
                .businessFieldName(businessField.getBusFieldName())
                .accountRole(businessField.getAccountRole())
                .cuCheck(Cucheck.N)
                .language(lang)
                .build();

        ContactUs savedContactUs = contactUsRepository.save(contactUs);
        String busFieldNum = savedContactUs.getBusinessField().getBusFieldNum();

        ContactUsResource contactUsResource = new ContactUsResource(contactUs);


        return ResponseEntity.ok(contactUsResource);
    }

    /**
     *  1대1 문의 수정 (수정시 cuAnswer, , cuMemo 값이 null이 아니면 mnNum값 필수)
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactUpdate(@RequestBody @Valid ContactUsUpdateDto updateDto,
                                        Errors errors,
                                        @PathVariable(name = "id", required = true) Long id,
                                        @PathVariable(name = "lang", required = true) String lang,
                                        @CurrentUser Account account) {

        if (account == null) {
            log.error("status = {}, message = {}", "403", "접근 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("접근 권한이 없습니다.","401"));
        }

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "1대1 문의 수정 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        Set<AccountRole> roles = account.getRoles();
        AccountRole currentRole = null;
        for (AccountRole role : roles) currentRole = AccountRole.valueOf(role.name());

        Optional<ContactUs> optionalContactUs = contactUsRepository.findById(id);
        if (!optionalContactUs.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 1대1 문의가 없습니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
        }
        ContactUs contactUs = optionalContactUs.get();

        if (!currentRole.equals(contactUs.getAccountRole()) && !"PERSON".equals(currentRole.name()) && !"ADMIN".equals(currentRole.name())) {
            log.error("status = {}, message = {}", "401", "접근 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("문의 접근 권한이 없습니다. 필요 권한 = " + contactUs.getAccountRole().name() + ", 현재 권한 = " + currentRole.name(),"403"));
        }



//        Optional<BusinessField> optionalBusinessField = businessFieldRepository.findByBusFieldNum(updateDto.getBusFieldNum());
//        if (!optionalBusinessField.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 사업분야가 없습니다. 사업분야 번호를 확인 해주세요.","400"));
//        }
//        String busFieldNum = optionalBusinessField.get().getBusFieldNum();


        if ("Y".equals(updateDto.getCuCheck())) {
            contactUs.setCuCheck(Cucheck.Y);
        }
        if ("N".equals(updateDto.getCuCheck())) {
            contactUs.setCuCheck(Cucheck.N);
        }

        //todo 수정시 매니저, 메모,

        //답변 또는 매니저가 null이 아니면...
//        if (updateDto.getCuAnswer() != null || updateDto.getMnNum() != null || updateDto.getCuMemo() != null) {
//            Optional<Manager> optionalManager = managerRepository.findByMnNum(updateDto.getMnNum());
//            if (!optionalManager.isPresent()) {
//                return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저가 없습니다. 매니저를 선택하세요. 사번 = "+updateDto.getMnNum(),"400"));
//            }
//            Manager manager = optionalManager.get();
//
//            contactUs.setMnNum(manager.getMnNum());
//            contactUs.setMnName(manager.getMnName());
//            contactUs.setManager(manager);
//            contactUs.setCuAnswer(updateDto.getCuAnswer());
//            contactUs.setCuMemo(contactUs.getCuMemo());
//        }

        contactUs.changeContactUs(updateDto, account.getAcName());

        ContactUs updatedContactUs = contactUsRepository.save(contactUs);

        ContactUsResource contactUsResource = new ContactUsResource(updatedContactUs);

//        if (contactUs.getManager() != null) {
//            contactUsResource.add(linkTo(methodOn(ContactUsController.class).getManager(id,lang)).withRel("get-manager(cu_id)"));
//        }

        return ResponseEntity.ok(contactUsResource);
    }

    /**
     * 매니저 불러오기
     */
//    @GetMapping(value = "/manager/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
//    public ResponseEntity getManager(@PathVariable(name = "id") Long id,
//                                     @PathVariable(name = "lang", required = true) String lang) {
//
//        Optional<ContactUs> optionalContactUs = contactUsRepository.findById(id);
//        if (!optionalContactUs.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
//        }
//
//        ContactUs contactUs = optionalContactUs.get();
//
//
////        Manager contactUsManager = contactUs.getManager();
////        if (contactUsManager == null) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("담당 매니저가 없습니다.","404"));
////        }
//
//        String mnNum = contactUsManager.getMnNum();
//
//        Optional<Manager> optionalManager = managerRepository.findByMnNum(mnNum);
//        if (!optionalManager.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 매니저 정보가 없습니다. 사번을 확인해주세요."));
//        }
//        Manager manager = optionalManager.get();
//
//        ManagerDto managerDto = getManagerDto(manager);
//
//        ManagerDtoResource managerDtoResource = new ManagerDtoResource(managerDto, lang);
//
//        return ResponseEntity.ok(managerDtoResource);
//    }
//
//    private static ManagerDto getManagerDto(Manager manager) {
//
//        ManagerDto managerDto = ManagerDto.builder()
//                .id(manager.getId())
//                .mnNum(manager.getMnNum())
//                .mnName(manager.getMnName())
//                .mnDeptNum(manager.getMnDepartment().getDeptNum())
////                .mnDepartment(manager.getMnDepartment().get)
//                .mnPosition(manager.getMnPosition())
//                .mnPhone(manager.getMnPhone())
//                .mnEmail(manager.getMnEmail())
//                .busFieldNum(manager.getBusinessField().getBusFieldName())
//                .build();
//        return managerDto;
//    }


}
