package com.daesung.api.contact.web;

import com.daesung.api.common.domain.Manager;
import com.daesung.api.common.repository.ManagerRepository;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.contact.domain.ContactUs;
import com.daesung.api.contact.domain.enumType.Cucheck;
import com.daesung.api.contact.repository.ContactUsRepository;
import com.daesung.api.contact.repository.condition.ContactSearchCondition;
import com.daesung.api.contact.resource.ContactUsListResource;
import com.daesung.api.contact.resource.ContactUsResource;
import com.daesung.api.contact.web.dto.ContactUsDto;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.resource.HistoryListResource;
import com.daesung.api.history.resource.HistoryResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/contact")
public class ContactUsController {

    private final ContactUsRepository contactUsRepository;
    private final ManagerRepository managerRepository;

    /**
     *  1대1 문의 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity contactListGet(Pageable pageable,
                                            PagedResourcesAssembler<ContactUs> assembler,
                                            @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                            @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                            @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                            @RequestParam(name = "size", required = false, defaultValue = "") String size) {

        ContactSearchCondition searchCondition = new ContactSearchCondition();

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

        Page<ContactUs> contactUsPage = contactUsRepository.searchContactList(searchCondition, pageable);
//        Page<ContactUs> contactUsPage = contactUsRepository.findAll(pageable);
        PagedModel<EntityModel<ContactUs>> pagedModel = assembler.toModel(contactUsPage, e -> new ContactUsListResource(e));

        return ResponseEntity.ok().body(pagedModel);
    }

    /**
     *  1대1 문의 등록
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactInsert(@RequestBody @Valid ContactUsDto contactUsDto,
                                        Errors errors,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        ContactUs contactUs = ContactUs.builder()
                .cuName(contactUsDto.getCuName())
                .cuEmail(contactUsDto.getCuEmail())
                .cuPhone(contactUsDto.getCuPhone())
                .cuContent(contactUsDto.getCuContent())
                .cuCheck(Cucheck.N)
                .language(lang)
                .build();

        ContactUs savedContactUs = contactUsRepository.save(contactUs);

        return ResponseEntity.ok(savedContactUs);
    }

    /**
     *  1대1 문의 단건 조회
     */

    //연혁 불러오기
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactGet(@PathVariable(name = "id", required = true) Long id,
                                     @PathVariable(name = "lang", required = true) String lang){

        Optional<ContactUs> optionalContactUs = contactUsRepository.findById(id);
        if (!optionalContactUs.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
        }
        ContactUs contactUs = optionalContactUs.get();

        return ResponseEntity.ok(new ContactUsResource(contactUs));
    }

    /**
     *  1대1 문의 등록
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity contactUpdate(@RequestBody @Valid ContactUsDto contactUsDto,
                                        Errors errors,
                                        @PathVariable(name = "id", required = true) Long id,
                                        @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<ContactUs> optionalContactUs = contactUsRepository.findById(id);
        if (!optionalContactUs.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 1대1 문의가 없습니다.","400"));
        }

        ContactUs contactUs = optionalContactUs.get();

        contactUs.updateContactUs(contactUsDto);

        if ("Y".equals(contactUsDto.getCuCheck())) {
            contactUs.setCuCheck(Cucheck.Y);
        }
        if ("N".equals(contactUsDto.getCuCheck())) {
            contactUs.setCuCheck(Cucheck.N);
        }

        //답변 또는 매니저가 null이 아니면...
        if (contactUsDto.getCuAnswer() != null || contactUsDto.getMnNum() != null) {
            Optional<Manager> optionalManager = managerRepository.findByMnNum(contactUsDto.getMnNum());
            if (!optionalManager.isPresent()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 매니저가 없습니다. 사번 = "+contactUsDto.getMnNum(),"400"));
            }
            Manager manager = optionalManager.get();
            contactUs.setManager(manager);
        }

        ContactUs updatedContactUs = contactUsRepository.save(contactUs);

        return ResponseEntity.ok(new ContactUsResource(updatedContactUs));
    }


}
