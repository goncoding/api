package com.daesung.api.popup.domain;

import com.daesung.api.popup.web.dto.PopupDto;
import com.daesung.api.utils.date.RegTimeEntity;
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
@ToString(exclude = "irYear")
@Builder
@Table(name = "ds_pop_up")
public class Popup extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pu_id")
    private Long id;

    private String puTitle;
    private LocalDate puStartDate;
    private LocalDate puEndDate;
    private String puSequence;
    @Column(length = 512)
    private String puFileOriginalName;
    @Column(length = 512)
    private String puFileSavedName;
    @Column(length = 1024)
    private String puFileSavedPath;
    @Column(length = 1024)
    private String puFileSummary;

    private String regUser;
    private String language;

    public void changeFileInfo(UploadFile uploadFile, PopupDto dto) {
        this.puFileOriginalName = uploadFile.getOriginName();
        this.puFileSavedName = uploadFile.getNewName();
        this.puFileSavedPath = uploadFile.getRealPath();
        this.puFileSummary = dto.getFileSummary();
    }

    public void deleteFileInfo(Popup popup) {
        this.puFileOriginalName = null;
        this.puFileSavedName = null;
        this.puFileSavedPath = null;
        this.puFileSummary = null;
    }

    public void changePopup(PopupDto dto) {
        this.puTitle = dto.getTitle();
        this.puStartDate = dto.getStartDate();
        this.puEndDate = dto.getEndDate();
        this.puSequence = dto.getSequence();
    }
}
