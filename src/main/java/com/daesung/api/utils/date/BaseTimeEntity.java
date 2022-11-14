package com.daesung.api.utils.date;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private String regDate;

    @LastModifiedDate
    private String updDate;

    @PrePersist //insert 이전 실행
    public void onPrePersist(){
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.updDate = this.regDate;
    }

    @PreUpdate //update 이전 실행
    public void onPreUpdate(){
        this.updDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }





}
