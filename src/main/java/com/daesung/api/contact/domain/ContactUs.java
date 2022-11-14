package com.daesung.api.contact.domain;

import com.daesung.api.contact.domain.enumType.Cucheck;
import com.daesung.api.contact.web.dto.ContactUsDto;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"businessField", "bus_field_id"})
@Builder
@Table(name = "ds_contact_us")
public class ContactUs extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id")
    private Long cuId;
    private String cuName;
    private String cuEmail;
    private String cuPhone;

    @Column(columnDefinition = "TEXT")
    private String cuContent;

    @Enumerated(EnumType.STRING)
    private Cucheck cuCheck = Cucheck.N;

    private String cuAnswer;

    @Column(columnDefinition = "TEXT")
    private String cuMemo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_id")
    private BusinessField businessField;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mn_id")
    private Manager manager;

    private String language;


    public void updateContactUs(ContactUsDto contactUsDto) {
        this.cuName = contactUsDto.getCuName();
        this.cuEmail = contactUsDto.getCuEmail();
        this.cuEmail = contactUsDto.getCuEmail();
        this.cuContent = contactUsDto.getCuContent();
        this.cuAnswer = contactUsDto.getCuAnswer();
        this.cuMemo = contactUsDto.getCuMemo();
    }
}
