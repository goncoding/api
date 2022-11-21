package com.daesung.api.ir.domain;

import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.utils.date.RegTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "irYear")
@Builder
@Table(name = "ds_ir_Info")
public class IrInfo extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ir_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private IrType irType;

    private String irTitle;
    @Column(length = 512)
    private String irFileOriginalName;
    @Column(length = 512)
    private String irFileSavedName;
    @Column(length = 1024)
    private String irFileSavedPath;
    private String regUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iy_id")
    private IrYear irYear;

    private String language;


    //연관관계편의 메소드
    public void setIrYear(IrYear irYear) {
        if (this.irYear != null) {
            this.irYear.getIrInfoList().remove(this);
        }
        this.irYear = irYear;
        if (!irYear.getIrInfoList().contains(this)) {
            irYear.addIrInfo(this);
        }
    }





}
