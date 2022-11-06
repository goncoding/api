package com.daesung.api.news.web.dto;

import com.daesung.api.news.domain.enumType.NbType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsForm {

    private String nbType; //NE("뉴스"), RE("보도");
    private String title;
    private String content;
    private String newCompany;
    private String link;
    private Long viewCnt;
    private String language;

    private MultipartFile thumbnailFile;
    private List<MultipartFile> newsFiles;


}
