package com.daesung.api.news.domain;

import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.news.domain.enumType.NbType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"newsFiles", "newsThumbnailFiles"})
@Builder
@Table(name = "ds_news_board")
public class News extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private NbType nbType; //NE("뉴스"), RE("보도");

    private String title;
    private String content;
    private Long viewCnt;
    private String newCompany; //회사명
    private String link;

    @JsonIgnore
    @OneToMany(mappedBy = "news")
    private List<NewsFile> newsFiles;

    @JsonIgnore
    @OneToMany(mappedBy = "news")
    private List<NewsThumbnailFile> newsThumbnailFiles;

    private String language;
    private String regUser;

    private String updUser;







}
