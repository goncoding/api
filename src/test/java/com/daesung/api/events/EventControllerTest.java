package com.daesung.api.events;

import com.daesung.api.common.BaseControllerTest;
import com.daesung.api.common.RestDocsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("api spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .endEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Content-Type","application/hal+json"))
//                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.PUBLISHED)))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update events"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name"),
                                fieldWithPath("description").description("description"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime"),
                                fieldWithPath("location").description("location"),
                                fieldWithPath("basePrice").description("basePrice"),
                                fieldWithPath("maxPrice").description("maxPrice"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
                        )

                        ))

        ;

    }

    @DisplayName("")
    @Test
    public void createEvent_bad() throws Exception{

        Event event = Event.builder()
                .name("spring")
                .description("api spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .endEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }


    @DisplayName("")
    @Test
    public void _테스트_bad_request_Empty_Input() throws Exception{

        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;

    }

    @DisplayName("입력값이 잘못 된 경우")
    @Test
    public void _테스트_bad_request_Empty_Input2() throws Exception{

        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("api spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,24,22,10))
                .endEventDateTime(LocalDateTime.of(2018,11,23,22,10))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.events").exists())
                .andDo(print())

        ;

    }

    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    @Test
    public void _테스트2() throws Exception{
        // given
        IntStream.range(0,30).forEach(this::generateEvent);

        mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

    }


    @DisplayName("기존의 이벤트를 하나 조회하기")
    @Test
    public void _테스트23() throws Exception{
        // given
        Event event = this.generateEvent(100);

        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andDo(print())
                .andDo(document("get-event"))
                ;
    }

    @DisplayName("이벤트를 정상적으로 수정")
    @Test
    public void _테스트_수정_ok() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String updated_event = "updated event";
        eventDto.setName(updated_event);

        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(updated_event))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @DisplayName("이벤트를 정상적으로 수정_비어있는 입력값")
    @Test
    public void _테스트_수정_empty() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("이벤트를 정상적으로 수정_틀린 입력값")
    @Test
    public void _테스트_수정_badrequest() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(200000);
        eventDto.setMaxPrice(1000);

        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    @DisplayName("이벤트를 정상적으로 수정_존재하지 않는 이벤트")
    @Test
    public void _테스트_수정_badrequest2() throws Exception{
        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        mockMvc.perform(put("/api/events/1234")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }


    private Event generateEvent(int i) {

        Event event = Event.builder()
                .name("event"+i)
                .description("api spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,10,22,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .endEventDateTime(LocalDateTime.of(2018,11,10,22,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 d2")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return eventRepository.save(event);

    }

}