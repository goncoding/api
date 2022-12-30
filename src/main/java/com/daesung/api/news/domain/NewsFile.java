package com.daesung.api.news.domain;

import com.daesung.api.utils.date.RegTimeEntity;
import com.daesung.api.news.domain.enumType.ShowYn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "news")
@Builder
@Table(name = "ds_news_file")
public class NewsFile extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_file_no")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @Column(length = 512)
    private String newsFileOriginalName;
    @Column(length = 1024)
    private String newsFileSavedPath;
    @Column(length = 512)
    private String newsFileSavedName;
//    private String regUser;

    @Enumerated(EnumType.STRING)
    private ShowYn showYn;

}
