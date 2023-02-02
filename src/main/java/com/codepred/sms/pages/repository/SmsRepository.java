package com.codepred.sms.pages.repository;

import com.codepred.sms.pages.model.SmsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository extends CrudRepository<SmsEntity, Long> {



}