package com.daesung.api.common.domain;

import com.daesung.api.utils.date.BaseTimeEntity;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(length = 1024)
    private String busThumbnail;





}
