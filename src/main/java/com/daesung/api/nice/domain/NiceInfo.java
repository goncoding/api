package com.daesung.api.nice.domain;

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
@Table(name = "ds_nice_Info")
public class NiceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nice_id")
    private Long id;
    private String compCode;
    private String reqNo;
    private String symKey;
    private String iv;
    private String hmacKey;

    @Column(length = 1024)
    private String enc_data;


}
