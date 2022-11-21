package com.daesung.api.ir.domain;

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
@Table(name = "ds_disclosure_Info")
public class DisClosureInfo extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "di_id")
    private Long id;

    private String diTitle;
    @Column(length = 512)
    private String diFileOriginalName;
    @Column(length = 512)
    private String diFileSavedName;
    @Column(length = 1024)
    private String diFileSavedPath;
    private String regUser;
    private String language;





}
