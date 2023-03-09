package com.codepred.sms.property.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

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

    private Date sendSmsDate;

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