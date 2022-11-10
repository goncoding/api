package com.daesung.api.history.domain;

import com.daesung.api.common.BaseTimeEntity;
import com.daesung.api.utils.upload.UploadFile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "ds_history")
public class History extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hi_id")
    private Long id;
    private String hiStartYear;
    private String hiEndYear;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 512)
    private String hiOriginFileName;
    @Column(length = 512)
    private String hiSaveFileName;
    @Column(length = 1024)
    private String hiFileSavedPath;

    private String language;

    private String regUser;
    private String updUser;

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeFileInfo(UploadFile uploadFile) {
        this.hiOriginFileName = uploadFile.getOriginName();
        this.hiSaveFileName = uploadFile.getNewName();
        this.hiFileSavedPath = uploadFile.getRealPath();
    }


}
