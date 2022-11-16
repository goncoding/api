package com.daesung.api.history.domain;

import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.history.web.dto.RecordDto;
import com.daesung.api.utils.date.BaseTimeEntity;
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
@Table(name = "ds_history_record")
public class HistoryRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hr_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private HrCategory hrCategory;

    private String hrCategoryName;
    private String hrTitle;
    private String hrContent;
    private String regUser; //사번
    private String updUser; //사번
    private String language;


    public void updateRecord(RecordDto recordDto, HistoryRecord historyRecord, String content, HrCategory enumCategory, String description) {

        this.hrCategory = enumCategory;
        this.hrCategoryName = description;
        this.hrTitle = recordDto.getHrTitle();
        this.hrContent = content;



    }

}
