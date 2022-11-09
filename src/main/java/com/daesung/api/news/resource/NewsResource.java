package com.daesung.api.news.resource;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.web.NewsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class NewsResource extends EntityModel<News> {

    public NewsResource(News news, Link... links) {
        super(news, links);
        add(linkTo(NewsController.class,news.getLanguage()).slash(news.getId()).withSelfRel());
    }

}
