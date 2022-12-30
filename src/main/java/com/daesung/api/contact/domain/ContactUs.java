package com.daesung.api.contact.domain;

import com.daesung.api.accounts.domain.Account;
import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.domain.Department;
import com.daesung.api.common.domain.enumType.ConsentStatus;
import com.daesung.api.contact.domain.enumType.Cucheck;
import com.daesung.api.contact.web.dto.ContactUsUpdateDto;
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
@ToString(exclude = {"businessField", "manager"})
@Builder
@Table(name = "ds_contact_us")
public class ContactUs extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id")
    private Long id;
    private String cuName;
    private String cuEmail;
    private String cuPhone;

    private String mnNum;

    private String mnName;

    private String businessFieldName;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @Column(columnDefinition = "TEXT")
    private String cuContent;

    @Enumerated(EnumType.STRING)
    private Cucheck cuCheck = Cucheck.N;

    private String cuAnswer;

    @Column(columnDefinition = "TEXT")
    private String cuMemo;

    @Enumerated(EnumType.STRING)
    private ConsentStatus consentStatus = ConsentStatus.N;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_id")
    private BusinessField businessField;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mn_id")
//    private Manager manager;

    private String acName;

    private String language;

    public void changeContactUs(ContactUsUpdateDto dto, String acName) {
        this.cuName = dto.getCuName();
        this.cuPhone = dto.getCuPhone();
        this.cuEmail = dto.getCuEmail();
        this.cuContent = dto.getCuContent();
        this.mnName = dto.getMnName();
        this.cuAnswer = dto.getCuAnswer();
        this.cuMemo = dto.getCuMemo();
        this.acName = acName;
    }

}
