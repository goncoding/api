package com.daesung.api.esg.domain;

import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.utils.date.RegTimeEntity;
import com.daesung.api.utils.upload.UploadFile;
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
@Table(name = "ds_esg")
public class Esg extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esg_id")
    private Long id;

    private String esgCategory;
    @Column(length = 512)
    private String esgFileOriginalName;
    @Column(length = 512)
    private String esgFileSavedName;
    @Column(length = 1024)
    private String esgFileSavedPath;
    private String language;


    public void changeFileInfo(UploadFile uploadFile) {

        this.esgFileOriginalName = uploadFile.getOriginName();
        this.esgFileSavedName = uploadFile.getNewName();
        this.esgFileSavedPath = uploadFile.getRealPath();
    }
}
