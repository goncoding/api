package com.daesung.api.common.domain;

import com.daesung.api.ethical.web.EthicalReportController;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "businessField")
@Builder
@Table(name = "ds_manager")
public class Manager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mn_id")
    private Long id;
    @Column(unique = true)
    private String mnNum;
    private String mnCategory;
    private String mnName;
    private String mnDepartment;
    private String mnPosition;
    private String mnPhone;
    private String mnEmail;
    private String regDate;
    private String language;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_id")
    private BusinessField businessField;


}
