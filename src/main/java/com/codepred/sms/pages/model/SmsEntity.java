package com.codepred.sms.pages.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table
public class SmsEntity {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CreatedDate
    @Column()
    private Date createdAt;

    private String phoneNumber;

    private boolean wasSent;

    private String pageUrl;

    private String pageId;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}