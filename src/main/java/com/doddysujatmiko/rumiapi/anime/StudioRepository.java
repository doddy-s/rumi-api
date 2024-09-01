package com.doddysujatmiko.rumiapi.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<StudioEntity, Long> {
    StudioEntity findByMalId(Integer malId);
}
