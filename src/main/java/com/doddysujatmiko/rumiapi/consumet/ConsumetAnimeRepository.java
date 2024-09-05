package com.doddysujatmiko.rumiapi.consumet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumetAnimeRepository extends JpaRepository<ConsumetAnimeEntity, Long> {
    ConsumetAnimeEntity findByConsumetId(String consumetId);
}
