package com.daesung.api.ethical.domain;

import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.common.domain.BusinessField;
import com.daesung.api.common.domain.Manager;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"businessField", "bus_field_no"})
@Builder
@Table(name = "ds_ethical_report")
public class EthicalReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ds_id")
    private Long dsId;
    private String dsName;
    private String dsEmail;
    private String dsPhone;
    private String dsTitle;
    @Column(columnDefinition = "TEXT")
    private String dsContent;
    private String dsCheck;
    private String dsAnswer;
    @Column(columnDefinition = "TEXT")
    private String dsMemo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_field_no")
    private BusinessField businessField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mn_id")
    private Manager manager;

    private String language;


}
