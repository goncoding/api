package com.daesung.api.common.domain;

import com.daesung.api.common.BaseTimeEntity;
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
    @Column(name = "bus_field_no")
    private Long id;
    private String busFieldName;
    private String busFieldInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    @Column(length = 1024)
    private String busThumbnail;





}
