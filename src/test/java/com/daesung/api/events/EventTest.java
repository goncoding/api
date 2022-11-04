package com.daesung.api.events;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EventTest {

    @DisplayName("")
    @Test
    public void _테스트() throws Exception{

        Event event = Event.builder()
                .build();

        assertThat(event).isNotNull();
    }

    @DisplayName("free ")
    @Test
    public void _테스트_free() throws Exception{
        //given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //when
        event.update();
        //then
        assertThat(event.isFree()).isTrue();

        //given
        Event event2 = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //when
        event2.update();
        //then
        assertThat(event2.isFree()).isFalse();

        //given
        Event event3 = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //when
        event3.update();
        //then
        assertThat(event3.isFree()).isFalse();

    }

    @DisplayName("offline ")
    @Test
    @MethodSource("paramsForTest")
    public void _테스트_offline(String parma1, boolean isOffline) throws Exception{
        //given
        Event event = Event.builder()
                .location("강남")
                .build();
        //when
        event.update();
        //then
        assertThat(isOffline).isTrue();

        //given
        Event event2 = Event.builder()
                .build();
        //when
        event2.update();
        //then
        assertThat(event2.isOffline()).isEqualTo(isOffline);

    }
    //self
    //profile




}