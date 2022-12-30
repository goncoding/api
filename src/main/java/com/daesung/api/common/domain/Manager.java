package com.daesung.api.common.domain;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.web.dto.ManagerDto;
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
@ToString(exclude = {"businessField","mnDepartment"})
@Builder
@Table(name = "ds_manager")
public class Manager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mn_id")
    private Long id;
//    @Column(unique = true)
    private String mnNum;
    private String mnName;
    private String businessFieldName;
    private String deptName;
    private String mnPosition;
    private String mnPhone;
    private String mnEmail;
    private String language;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department mnDepartment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_id")
    private BusinessField businessField;


    public void insertManager(ManagerDto managerDto, String lang) {
             this.mnNum = managerDto.getMnNum();
             this.mnName = managerDto.getMnName();
             this.mnEmail = managerDto.getMnEmail();
             this.mnPhone = managerDto.getMnPhone();
             this.mnPosition = managerDto.getMnPosition();
             this.language = lang;

    }



}
