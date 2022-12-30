package com.daesung.api.common.domain;

import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.utils.date.RegTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "ds_department")
public class Department extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;
    private String deptName;
    private String deptNum;
    private String deptInfo;


    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;


}
