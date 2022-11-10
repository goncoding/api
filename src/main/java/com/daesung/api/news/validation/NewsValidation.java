package com.daesung.api.news.validation;

import com.daesung.api.events.EventDto;
import com.daesung.api.news.web.dto.NewsDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class NewsValidation {

    /**
     * 뉴스시 title, content , 필수값...
     * 보도시 link, companyname 필수...
     */
    public void validate(NewsDto newsDto, Errors errors) {
        String title = newsDto.getTitle();
        String content = newsDto.getContent();
        String link = newsDto.getLink();
        String newCompany = newsDto.getNewCompany();

        if ("NE".equals(newsDto.getNbType())) {
            if (title == null) {
                errors.rejectValue("title","wrongValue","뉴스(NE) 선택시 title 필수입니다.");
            }
            if (content == null) {
                errors.rejectValue("content","wrongValue","뉴스(NE) 선택시 content 필수입니다.");
            }
            if (link != null) {
                errors.rejectValue("link","wrongValue","뉴스(NE) 선택시 link 입력 불가입니다.");
            }
            if (newCompany != null) {
                errors.rejectValue("content","wrongValue","뉴스(NE) 선택시 newCompany 입력 불가입니다.");
            }
        }
        if ("RE".equals(newsDto.getNbType())) {
            if (link == null) {
                errors.rejectValue("link","wrongValue","보도(RE) 선택시 link 필수입니다.");
            }
            if (newCompany == null) {
                errors.rejectValue("content","wrongValue","보도(RE) 선택시 newCompany 필수입니다.");
            }
            if (content != null) {
                errors.rejectValue("content","wrongValue","보도(RE) 선택시 content 입력 불가입니다.");
            }
        }

    }

}
