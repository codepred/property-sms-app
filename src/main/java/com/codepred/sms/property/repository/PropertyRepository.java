package com.codepred.sms.property.repository;

import com.codepred.sms.property.model.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    @Query(value = "SELECT * FROM property WHERE wassent = false", nativeQuery = true)
    List<PropertyEntity> getAllToSent();


    @Query(value = "SELECT * FROM property WHERE phone_number=:phone", nativeQuery = true)
    PropertyEntity getByPhone(@Param("phone") String  phone);

}