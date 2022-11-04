package com.daesung.api.news.domain;

import com.daesung.api.common.BaseTimeEntity;
import com.daesung.api.news.domain.enumType.NbType;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"newsFile", "newsThumbnailFile"})
@Builder
@Table(name = "ds_news_board")
public class News extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nb_no")
    private Long id;

    private NbType nbType; //NE("뉴스"), RE("보도");
    private String title;
    private String content;
    private String viewCnt;
    private String newCompany; //회사명
    private String link;

    @OneToMany(mappedBy = "news")
    private List<NewsFile> newsFiles;

    @OneToOne(mappedBy = "news")
    private NewsThumbnailFile newsThumbnailFile;

    private String language;
    private String regUser;
    private String updUser;







}
