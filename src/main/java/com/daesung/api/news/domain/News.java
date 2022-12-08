package com.daesung.api.news.domain;

import com.daesung.api.news.web.dto.NewsDto;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.news.domain.enumType.NbType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer viewCnt;
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

    private LocalDate selectRegDate; //작성일 추가


    public void updateNews(NewsDto newsDto) {
        //link, newCompany 초기화
        this.newCompany = null;
        this.link = null;
        this.nbType = NbType.NE;
        this.title = newsDto.getTitle();
        this.content = newsDto.getContent();
        this.selectRegDate = newsDto.getSelectRegDate();
    }


    public void updateReport(NewsDto newsDto) {
        //content 초기화
        this.content = null;
        this.nbType = NbType.RE;
        this.title = newsDto.getTitle();
        this.newCompany = newsDto.getNewCompany();
        this.link = newsDto.getLink();
        this.selectRegDate = newsDto.getSelectRegDate();
    }
}
