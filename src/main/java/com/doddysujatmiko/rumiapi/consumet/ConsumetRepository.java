package com.doddysujatmiko.rumiapi.consumet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumetRepository extends JpaRepository<ConsumetEntity, Long> {
    ConsumetEntity findByConsumetId(String consumetId);
}
