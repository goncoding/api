package com.daesung.api.news.domain;

import com.daesung.api.common.RegTimeEntity;
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
@Table(name = "ds_news_thumbnail_file")
public class NewsThumbnailFile extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumbnail_file_no")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @Column(length = 512)
    private String thumbnailFileOriginalName;
    @Column(length = 1024)
    private String thumbnailFileSavedPath;
    @Column(length = 512)
    private String thumbnailFileSavedName;
    private String regUser;


}
