package com.codepred.sms.pages.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

import jakarta.persistence.*;



import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "property")
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column
    private Date createdAt;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "wassent")
    private boolean wasSent;

    @Column(unique = true)
    private String pageUrl;

    private String area;

    private String pageId;

    public String getPhoneNumber() {
        return phoneNumber;
    }
}