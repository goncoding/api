package com.daesung.api.common.domain;

import com.daesung.api.accounts.domain.enumType.AccountRole;
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
@ToString(exclude = "business")
@Builder
@Table(name = "ds_bus_field")
public class BusinessField extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_field_id")
    private Long id;
    private String busFieldName;
    private String busFieldNum;
    private String busFieldInfo;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;


}
