package com.daesung.api.ethical.domain;

import com.daesung.api.common.domain.enumType.ConsentStatus;
import com.daesung.api.ethical.domain.enumType.ErCheck;
import com.daesung.api.ethical.web.dto.EthicalReportUpdateDto;
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
@Table(name = "ds_ethical_report")
public class EthicalReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "er_id")
    private Long id;
    private String erName;
    private String erEmail;
    private String erPhone;
    private String erTitle;

    @Column(columnDefinition = "TEXT")
    private String erContent;

    @Enumerated(EnumType.STRING)
    private ErCheck erCheck = ErCheck.N;

    private String mnNum;
    private String mnName;
    private String erAnswer;
    @Column(columnDefinition = "TEXT")
    private String erMemo;

    @Enumerated(EnumType.STRING)
    private ConsentStatus consentStatus = ConsentStatus.N;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_id")
    private BusinessField businessField;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mn_id")
    private Manager manager;

    private String language;


    public void changeAnswer(EthicalReportUpdateDto dto, Manager manager) {
        this.manager = manager;
        this.erAnswer = dto.getErAnswer();
        this.erMemo = dto.getErMemo();
        this.mnName = manager.getMnName();
        this.mnNum = manager.getMnNum();
    }


    public void changeReport(EthicalReportUpdateDto dto) {
        this.erName = dto.getErName();
        this.erEmail = dto.getErEmail();
        this.erPhone = dto.getErPhone();
        this.erTitle = dto.getErTitle();
        this.erContent = dto.getErContent();
    }
}
