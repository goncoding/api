package com.daesung.api.ir.domain;

import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.ir.web.dto.IrInfoDto;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.utils.date.RegTimeEntity;
import com.daesung.api.utils.upload.UploadFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Long frontId;

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
    private String year;

    @JsonIgnore
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

    public void updateIrInfo(IrType irType, IrYear irYear, String year, String title) {
        this.irType = irType;
        this.irYear = irYear;
        this.year = year;
        this.irTitle = title;
    }

    public void updateFileIrInfo(UploadFile uploadFile) {
        this.irFileOriginalName = uploadFile.getOriginName();
        this.irFileSavedName = uploadFile.getNewName();
        this.irFileSavedPath = uploadFile.getRealPath();
    }
}
