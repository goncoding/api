package com.daesung.api.history.domain;

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

    private String regUser;
    private String updUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hi_id")
    private History history;


}
