package com.codepred.sms.pages.repository;

import com.codepred.sms.pages.model.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    @Query(value = "SELECT * FROM property WHERE wassent = false", nativeQuery = true)
    List<PropertyEntity> getAllToSent();

}