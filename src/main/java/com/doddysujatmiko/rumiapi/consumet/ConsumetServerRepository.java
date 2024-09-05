package com.doddysujatmiko.rumiapi.consumet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumetServerRepository extends JpaRepository<ConsumetServerEntity, Long> {
    ConsumetServerEntity findByUrl(String url);
}
