package com.daesung.api.ir.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.resource.HistoryListResource;
import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.repository.IrYearRepository;
import com.daesung.api.ir.resource.IrYearResource;
import com.daesung.api.ir.web.dto.IrYearDto;
import com.daesung.api.ir.web.dto.IrYearUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/ir-year")
public class IrYearController {


    private final IrYearRepository irYearRepository;

    /**
     * IR_연도관리 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irYearListGet(Pageable pageable,
                                        @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                        @RequestParam(name = "size", required = false, defaultValue = "") String size,
                                        @PathVariable(name = "lang", required = true) String lang) {

        Sort sort = Sort.by("iyYear").descending();
        List<IrYear> irYearList = irYearRepository.findAll(sort);
        return ResponseEntity.ok().body(irYearList);
    }


    /**
     * IR_연도관리 단건 조회
     */
    @GetMapping(value = {"/{id}"}, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irYearGet(@PathVariable(value = "id", required = true) Long id,
                                    @PathVariable(value = "lang", required = true) String lang) {

        Optional<IrYear> optionalIrYear = irYearRepository.findById(id);
        if (!optionalIrYear.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 IR 연도 정보가 없습니다.", "400"));
        }

        IrYear irYear = optionalIrYear.get();

        IrYearResource irYearResource = new IrYearResource(irYear);
        irYearResource.add(linkTo(methodOn(IrYearController.class).irYearInsert(null,null,lang)).withRel("insert-irYear"));
        irYearResource.add(linkTo(IrYearController.class, lang).withRel("list-irYear"));

        return ResponseEntity.ok(irYearResource);
    }


    /**
     * IR_연도관리 등록
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irYearInsert(@RequestBody @Valid IrYearDto irYearDto, Errors errors,
                                       @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<IrYear> optionalIrYear = irYearRepository.findByIyYear(irYearDto.getIyYear());
        if (optionalIrYear.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("연도가 중복됩니다. 변경 해주세요.", "400"));
        }

        IrYear irYear = IrYear.builder()
                .iyYear(irYearDto.getIyYear())
                .regUser(irYearDto.getRegUser())
                .language(lang)
                .build();

        IrYear savedIrYear = irYearRepository.save(irYear);

        return ResponseEntity.ok().body(savedIrYear);
    }

    /**
     * IR_연도관리 수정
     */
    @PutMapping(value = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irYearUpdate(@RequestBody @Valid IrYearUpdateDto updateDto,
                                       @PathVariable(value = "id", required = true) Long id,
                                       @PathVariable(value = "lang", required = true) String lang) {

        Optional<IrYear> optionalIrYear = irYearRepository.findById(id);
        if (!optionalIrYear.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 IR 연도 정보가 없습니다.", "400"));
        }
        IrYear irYear = optionalIrYear.get();

        Optional<IrYear> yearOptional = irYearRepository.findByIyYear(updateDto.getIyYear());
        if (yearOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("연도가 중복됩니다. 변경 해주세요.", "400"));
        }

        irYear.changeIrYear(updateDto);

        IrYear updatedIrYear = irYearRepository.save(irYear);

        return ResponseEntity.ok(updatedIrYear);
    }

    /**
     * IR_연도관리 삭제
     */
    @DeleteMapping(value = "{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irYearDelete(@PathVariable(value = "id", required = true) Long id,
                                       @PathVariable(value = "lang", required = true) String lang) {

        Optional<IrYear> optionalIrYear = irYearRepository.findById(id);
        if (!optionalIrYear.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 IR 연도 정보가 없습니다.", "400"));
        }

        IrYear irYear = optionalIrYear.get();

        irYearRepository.deleteById(irYear.getId());

        return ResponseEntity.ok(irYear.getIyYear() + "년 연도가 삭제되었습니다.");
    }





}
