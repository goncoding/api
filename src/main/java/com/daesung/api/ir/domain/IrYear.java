package com.daesung.api.ir.domain;

import com.daesung.api.ir.web.dto.IrYearUpdateDto;
import com.daesung.api.utils.date.RegTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "irInfoList")
@Builder
@Table(name = "ds_ir_year")
public class IrYear extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iy_id")
    private Long id;
    private String iyYear;
    private String regUser;
    private String language;

    @JsonIgnore
    @OneToMany(mappedBy = "irYear")
    private List<IrInfo> irInfoList = new ArrayList<>();

    //연관관계편의 메소드
    public void addIrInfo(IrInfo irInfo) {
        this.irInfoList.add(irInfo);
        if (irInfo.getIrYear() != this) {
            irInfo.setIrYear(this);
        }
    }


    public void changeIrYear(IrYearUpdateDto dto) {
        this.iyYear = dto.getIyYear();
    }
}
