package com.daesung.api.history.domain;

import com.daesung.api.utils.date.RegTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "ds_history_record_file")
public class HistoryRecordFile extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hr_file_id")
    private Long id;

    private String hrFileOriginalName;
    private String hrFileSavedPath;
    private String hrFileSavedName;
    private String hrFileSeq;
//    private String regUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private HistoryRecord historyRecord;



}
