package com.daesung.api.history.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "history")
@Builder
@Table(name = "ds_history_detail")
public class HistoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hd_id")
    private Long id;
    private String hdYear;
    private String hdMonth;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer hdSequence;
    private String language;
    private String regUser;
    private String updUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hi_id")
    private History history;

    public void plusSequence() {
        this.hdSequence++;
    }

    public void minusSequence() {
        this.hdSequence--;
    }



}
