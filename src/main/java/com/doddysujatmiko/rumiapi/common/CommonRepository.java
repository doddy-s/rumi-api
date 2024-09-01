package com.doddysujatmiko.rumiapi.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonRepository extends JpaRepository<CommonEntity, Long> {
    CommonEntity findByKey(String key);
}
